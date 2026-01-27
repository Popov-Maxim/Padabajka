package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReactionEntity
import com.padabajka.dating.core.data.uuid
import com.padabajka.dating.core.domain.replaced
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.SyncResult
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.NoAuthorisedUserException
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.ParentMessage
import com.padabajka.dating.core.repository.api.model.messenger.RawMessage
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.toEditRequest
import com.padabajka.dating.feature.messenger.data.message.model.toEntity
import com.padabajka.dating.feature.messenger.data.message.model.toMarkAsReadRequest
import com.padabajka.dating.feature.messenger.data.message.model.toSendRequest
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.addReaction
import com.padabajka.dating.feature.messenger.data.message.source.local.removeReaction
import com.padabajka.dating.feature.messenger.data.message.source.local.toSendRequestDto
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class MessageRepositoryImpl(
    private val authRepository: AuthRepository,
    private val localMessageDataSource: LocalMessageDataSource,
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val personRepository: PersonRepository
) : MessageRepository {

    private val myRawPersonId: String?
        get() = authRepository.currentAuthState
            .let { (it as? LoggedIn)?.userId?.raw }

    private val myPersonId: PersonId
        get() = PersonId(myRawPersonId ?: throw NoAuthorisedUserException)

    override fun messages(chatId: ChatId): Flow<List<Message>> =
        localMessageDataSource.messages(chatId.raw).map { messages ->
            messages.map { it.toDomain() }
        }

    override fun lastMessage(chatId: ChatId): Flow<Message?> {
        return localMessageDataSource.lastMessage(chatId.raw)
            .map { it?.toDomain() }
    }

    override suspend fun unreadMessagesCount(chatId: ChatId): Int {
        return localMessageDataSource.unreadMessagesCount(chatId.raw, myPersonId.raw)
    }

    override suspend fun sendMessage(
        chatId: ChatId,
        content: String,
        parentMessageId: MessageId?
    ) {
        val currentTime = nowMilliseconds()

        val messageEntry = MessageEntry(
            id = uuid(),
            chatId = chatId.raw,
            authorId = myPersonId.raw,
            content = content,
            creationTime = currentTime,
            messageStatus = MessageStatus.Sending,
            readAt = null,
            readSynced = true,
            reactions = listOf(),
            parentMessageId = parentMessageId?.raw,
            editedAt = null,
            editSynced = true
        )

        localMessageDataSource.addMessage(messageEntry)

        trySendMessageToRemote(messageEntry)
    }

    override suspend fun deleteMessage(chatId: ChatId, messageId: MessageId) {
        runCatching {
            remoteMessageDataSource.deleteMessage(chatId, messageId)
        } // TODO(message): add exception handling
        localMessageDataSource.deleteMessage(messageId.raw)
    }

    override suspend fun deleteLocalMessage(chatId: ChatId, messageId: MessageId) {
        localMessageDataSource.deleteMessage(messageId.raw)
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun editMessage(chatId: ChatId, messageId: MessageId, content: String) {
        val updatedMessage = localMessageDataSource.updateMessage(messageId.raw) {
            it.copy(editedAt = nowMilliseconds(), editSynced = false, content = content)
        }

        try {
            val editMessageDto = updatedMessage.toEditRequest()
            val updatedMessageDto = remoteMessageDataSource.editMessage(
                chatId,
                messageId,
                editMessageDto
            )

            localMessageDataSource.updateMessage(messageId.raw) {
                updatedMessageDto.toEntity()
            }
        } catch (e: Throwable) {
            // TODO: retry read updating
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun readMessage(messageId: MessageId) {
        val message = localMessageDataSource.message(messageId.raw)
        if (message.readAt != null || message.authorId == myRawPersonId) return
        val updatedMessage = localMessageDataSource.updateMessage(messageId.raw) {
            it.copy(readAt = nowMilliseconds(), readSynced = false)
        }

        try {
            val (chatId, request) = updatedMessage.toMarkAsReadRequest()

            remoteMessageDataSource.readMessages(chatId, request)

            localMessageDataSource.updateMessage(messageId.raw) {
                it.copy(readSynced = true)
            }
        } catch (e: Throwable) {
            // TODO: retry read updating
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun reactToMessage(
        messageId: MessageId,
        reaction: MessageReaction.Value
    ) {
        val reaction = MessageReactionEntity(
            author = myPersonId.raw,
            value = reaction,
            time = nowMilliseconds(),
        )

        val updatedMessage = localMessageDataSource.updateMessage(messageId.raw) {
            it.addReaction(reaction)
        }

        try {
            val chatId = updatedMessage.chatId.let(::ChatId) // TODO: add chatId in method

            val updatedReaction =
                remoteMessageDataSource.sendReaction(chatId, messageId, reaction.toSendRequestDto())

            localMessageDataSource.updateMessage(messageId.raw) {
                it.copy(reactions = it.reactions?.replaced(reaction, updatedReaction.toEntity()))
            }
        } catch (e: Throwable) {
            // TODO: retry sending reaction
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun removeReactToMessage(
        messageId: MessageId
    ) {
        val updatedMessage = localMessageDataSource.updateMessage(messageId.raw) { message ->
            message.removeReaction { it.author == myPersonId.raw }
        } // TODO(messenger): add safely remove reaction

        try {
            val chatId = updatedMessage.chatId.let(::ChatId)

            remoteMessageDataSource.removeReaction(chatId, messageId)
        } catch (e: Throwable) {
            // TODO: retry sending reaction
        }
    }

    override suspend fun loadMessages(
        chatId: ChatId,
        beforeMessageId: MessageId,
        count: Int
    ) {
        val messageSyncResponse = remoteMessageDataSource.getMessages(chatId, beforeMessageId.raw, count)
        updateMessageDto(messageSyncResponse.messages)
    }

    override suspend fun loadMessages(
        chatId: ChatId,
        count: Int
    ): SyncResult {
        val messageSyncResponse = remoteMessageDataSource.getMessages(chatId, null, count)
        updateMessageDto(messageSyncResponse.messages)

        return SyncResult(messageSyncResponse.lastEventNumber)
    }

    override suspend fun syncMessages(chatId: ChatId, lastEventNumber: Long): SyncResult {
        val messageSyncResponse = remoteMessageDataSource.getMessages(chatId, lastEventNumber)
        updateMessageDto(messageSyncResponse.messages)

        return SyncResult(messageSyncResponse.lastEventNumber)
    }

    private suspend fun updateMessageDto(messageDto: List<MessageDto>) {
        val messageEntities = messageDto.filterIsInstance<MessageDto.Existing>().map { it.toEntity() }
        val messageIdsForDeleted = messageDto.filterIsInstance<MessageDto.Deleted>().map { it.id }
        localMessageDataSource.updateMessages(messageEntities, messageIdsForDeleted)
    }

    override suspend fun addLocalMessage(chatId: ChatId, message: RawMessage) {
        val messageEntry = message.toEntity(chatId)
        localMessageDataSource.addMessage(messageEntry)
    }

    override suspend fun updateLocalMessage(
        chatId: ChatId,
        messageId: MessageId,
        update: (RawMessage) -> RawMessage
    ) {
        localMessageDataSource.updateMessage(messageId.raw) { messageEntry ->
            val message = messageEntry.toRawMessage()
            val updatedMessage = update(message)
            messageEntry.merge(updatedMessage)
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private suspend fun trySendMessageToRemote(messageEntry: MessageEntry) {
        try {
            val chatId = messageEntry.chatId.run(::ChatId)
            val updatedMessageDto =
                remoteMessageDataSource.sendMessage(chatId, messageEntry.toSendRequest())

            localMessageDataSource.updateMessage(messageEntry.id) { updatedMessageDto.toEntity() }
        } catch (e: Throwable) {
            // TODO: retry sending
            localMessageDataSource.updateMessage(messageEntry.id) {
                it.copy(messageStatus = MessageStatus.FailedToSend)
            }
        }
    }

    private suspend fun MessageEntry.toDomain(): Message {
        val parentMessage = parentMessageId?.let { parentId ->
            val parentMessageDto = localMessageDataSource.message(parentId)
            ParentMessage(
                id = MessageId(parentMessageDto.id),
                direction = direction(parentMessageDto.authorId),
                content = parentMessageDto.content
            )
        }

        val domainReactions = reactions?.map {
            val personId = PersonId(it.author)
            val person = personRepository.getPerson(personId)
            MessageReaction(
                author = person,
                value = it.value,
                time = it.time,
                reactionSynced = it.reactionSynced
            )
        }

        return Message(
            id = MessageId(id),
            direction = direction(authorId),
            content = content,
            creationTime = localDateTime(creationTime),
            status = messageStatus,
            readAt = readAt?.run(::localDateTime),
            reactions = domainReactions ?: listOf(),
            parentMessage = parentMessage,
            editedAt = editedAt
        )
    }

    private fun direction(authorId: String): MessageDirection {
        return if (authorId == myPersonId.raw) {
            MessageDirection.OUTGOING
        } else {
            MessageDirection.INCOMING
        }
    }

    private fun nowMilliseconds(): Long = Clock.System.now().toEpochMilliseconds()

    private fun localDateTime(millis: Long): LocalDateTime =
        Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
}
