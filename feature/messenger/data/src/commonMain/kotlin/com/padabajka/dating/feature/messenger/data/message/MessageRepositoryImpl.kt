package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.core.data.uuid
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.NoAuthorisedUserException
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.ParentMessage
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class MessageRepositoryImpl(
    authRepository: AuthRepository,
    private val localMessageDataSource: LocalMessageDataSource,
    private val remoteMessageDataSource: RemoteMessageDataSource
) : MessageRepository {

    private val myRawPersonId: String? = authRepository.currentAuthState
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

    override suspend fun sendMessage(
        chatId: ChatId,
        content: String,
        parentMessageId: MessageId?
    ) {
        val currentTime = nowMilliseconds()

        val messageDto = MessageDto(
            id = uuid(),
            chatId = chatId.raw,
            authorId = myPersonId.raw,
            content = content,
            creationTime = currentTime,
            messageStatus = MessageStatus.Sending,
            readSynced = true,
            reaction = null,
            reactionSynced = true,
            parentMessageId = parentMessageId?.raw
        )

        localMessageDataSource.addMessage(messageDto)

        trySendMessageToRemote(messageDto)
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun readMessage(messageId: MessageId) {
        localMessageDataSource.updateMessage(messageId.raw) {
            it.copy(messageStatus = MessageStatus.Read, readSynced = false)
        }

        try {
            remoteMessageDataSource.readMessages(messageId.raw)

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
        reaction: MessageReaction
    ) {
        localMessageDataSource.updateMessage(messageId.raw) {
            it.copy(reaction = reaction, reactionSynced = false)
        }

        try {
            remoteMessageDataSource.sendReaction(messageId.raw, reaction.toString())

            localMessageDataSource.updateMessage(messageId.raw) {
                it.copy(reactionSynced = true)
            }
        } catch (e: Throwable) {
            // TODO: retry sending reaction
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private suspend fun trySendMessageToRemote(messageDto: MessageDto) {
        try {
            val updatedMessageDto =
                remoteMessageDataSource.sendMessage(messageDto.chatId, messageDto.content)

            localMessageDataSource.updateMessage(messageDto.id) { updatedMessageDto }
        } catch (e: Throwable) {
            // TODO: retry sending
            localMessageDataSource.updateMessage(messageDto.id) {
                it.copy(messageStatus = MessageStatus.FailedToSend)
            }
        }
    }

    private suspend fun MessageDto.toDomain(): Message {
        val parentMessage = parentMessageId?.let { parentId ->
            val parentMessageDto = localMessageDataSource.message(parentId)
            ParentMessage(
                id = MessageId(parentMessageDto.id),
                direction = direction(parentMessageDto.authorId),
                content = parentMessageDto.content
            )
        }

        return Message(
            id = MessageId(id),
            direction = direction(authorId),
            content = content,
            creationTime = localDateTime(creationTime),
            status = messageStatus,
            reaction = reaction,
            parentMessage = parentMessage
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
