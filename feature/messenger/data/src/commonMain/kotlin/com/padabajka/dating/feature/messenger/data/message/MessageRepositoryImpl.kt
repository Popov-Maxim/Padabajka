package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReactionEntity
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.core.data.uuid
import com.padabajka.dating.core.domain.replaced
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.SyncResult
import com.padabajka.dating.core.repository.api.model.auth.NoAuthorisedUserException
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.ParentMessage
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.messenger.data.message.model.ChatReadEventResponse
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.toEditRequest
import com.padabajka.dating.feature.messenger.data.message.model.toEntity
import com.padabajka.dating.feature.messenger.data.message.model.toSendRequest
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatReadStateDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.addReaction
import com.padabajka.dating.feature.messenger.data.message.source.local.removeReaction
import com.padabajka.dating.feature.messenger.data.message.source.local.toEntity
import com.padabajka.dating.feature.messenger.data.message.source.local.toSendRequestDto
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class MessageRepositoryImpl(
    private val authRepository: AuthRepository,
    private val localMessageDataSource: LocalMessageDataSource,
    private val localChatReadStateDataSource: LocalChatReadStateDataSource,
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val personRepository: PersonRepository,
    private val readMessageManager: ReadMessageManager
) : MessageRepository {

    private val myPersonId: UserId
        get() = authRepository.currentAuthState.userIdOrNull() ?: throw NoAuthorisedUserException

    override fun messages(chatId: ChatId): Flow<List<Message>> {
        return combine(
            localMessageDataSource.messages(chatId.raw),
            localChatReadStateDataSource.readStates(chatId.raw)
        ) { messages, readStates ->
            messages.map { messageEntry ->
                messageEntry.combineWithReadStates(readStates)
            }
        }
    }

    override fun lastMessage(chatId: ChatId): Flow<Message?> {
        return combine(
            localMessageDataSource.lastMessage(chatId.raw),
            localChatReadStateDataSource.readStates(chatId.raw)
        ) { messageEntry, readStates ->
            messageEntry?.combineWithReadStates(readStates)
        }
    }

    override suspend fun unreadMessagesCount(chatId: ChatId): Int {
        val lastReadEvent = localChatReadStateDataSource.lastReadEvent(chatId.raw, myPersonId)
        return localMessageDataSource.unreadMessagesCount(chatId.raw, myPersonId.raw, lastReadEvent)
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

    override suspend fun readMessage(messageId: MessageId) {
        readMessageManager.markRead(messageId)
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
            reactionSynced = false
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

    override suspend fun messageReactions(messageId: MessageId): List<MessageReaction> {
        return localMessageDataSource.message(messageId.raw)
            .reactions
            ?.map { it.toDomain() } ?: listOf()
    }

    override suspend fun loadMessages(
        chatId: ChatId,
        beforeMessageId: MessageId,
        count: Int
    ) {
        val messageSyncResponse =
            remoteMessageDataSource.getMessages(chatId, beforeMessageId.raw, count)
        updateMessageDto(messageSyncResponse.messages)
    }

    override suspend fun loadMessages(
        chatId: ChatId,
        count: Int
    ): SyncResult {
        val messageSyncResponse = remoteMessageDataSource.getMessages(chatId, null, count)
        updateMessageDto(messageSyncResponse.messages)
        updateReadEventDto(messageSyncResponse.readEvents)

        return SyncResult(messageSyncResponse.lastEventNumber, messageSyncResponse.lastReadEventLogNumber)
    }

    override suspend fun syncMessages(
        chatId: ChatId,
        lastEventNumber: Long,
        lastReadEventNumber: Long
    ): SyncResult {
        val messageSyncResponse = remoteMessageDataSource.getMessages(
            chatId = chatId,
            fromEventNumber = lastEventNumber,
            fromReadEventNumber = lastReadEventNumber
        )
        updateMessageDto(messageSyncResponse.messages)
        updateReadEventDto(messageSyncResponse.readEvents)

        return SyncResult(messageSyncResponse.lastEventNumber, messageSyncResponse.lastReadEventLogNumber)
    }

    private suspend fun updateMessageDto(messageDto: List<MessageDto>) {
        val messageEntities =
            messageDto.filterIsInstance<MessageDto.Existing>().map { it.toEntity() }
        val messageIdsForDeleted = messageDto.filterIsInstance<MessageDto.Deleted>().map { it.id }
        localMessageDataSource.updateMessages(messageEntities, messageIdsForDeleted)
    }

    private suspend fun updateReadEventDto(messageDto: List<ChatReadEventResponse>) {
        val messageEntities =
            messageDto.map { it.toEntity() }
        localChatReadStateDataSource.addMessageReadEvents(messageEntities)
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

    private suspend fun MessageEntry.combineWithReadStates(
        readStates: List<MessageReadEventEntry>
    ): Message {
        val lastReadState = readStates
            .asSequence()
            .filter { it.userId != this.authorId }
            .filter { it.lastReadMessageTime >= creationTime }
            .minByOrNull { it.lastReadMessageTime }

        return this.toDomain(lastReadState?.readAt)
    }

    private suspend fun MessageEntry.toDomain(readAt: Long?): Message {
        val parentMessage = parentMessageId?.let { parentId ->
            val parentMessageDto = localMessageDataSource.message(parentId)
            ParentMessage(
                id = MessageId(parentMessageDto.id),
                direction = direction(parentMessageDto.authorId),
                content = parentMessageDto.content
            )
        }

        val domainReactions = reactions?.map { it.toDomain() }

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

    private suspend fun MessageReactionEntity.toDomain(): MessageReaction {
        val personId = PersonId(this.author)
        val person = personRepository.getPerson(personId)
        return MessageReaction(
            author = person,
            value = this.value,
            time = this.time,
            reactionSynced = this.reactionSynced
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
