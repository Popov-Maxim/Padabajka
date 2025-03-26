package com.padabajka.dating.feature.messenger.data

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
import com.padabajka.dating.feature.messenger.data.model.MessageDto
import com.padabajka.dating.feature.messenger.data.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.source.remote.RemoteMessageDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MessageRepositoryImpl(
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

    override suspend fun sendMessage(
        chatId: ChatId,
        content: String,
        parentMessageId: MessageId?
    ) {
        val currentTime = nowMilliseconds()

        val messageDto = localMessageDataSource.addMessage(
            MessageDto(
                id = null,
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
        )

        trySendMessageToRemote(messageDto)
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun readMessage(chatId: ChatId, messageId: MessageId) {
        localMessageDataSource.updateMessage(chatId.raw, messageId.raw) {
            it.copy(messageStatus = MessageStatus.Read, readSynced = false)
        }

        try {
            remoteMessageDataSource.readMessages(chatId.raw, messageId.raw)

            localMessageDataSource.updateMessage(chatId.raw, messageId.raw) {
                it.copy(readSynced = true)
            }
        } catch (e: Throwable) {
            // TODO: retry read updating
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun reactToMessage(
        chatId: ChatId,
        messageId: MessageId,
        reaction: MessageReaction
    ) {
        localMessageDataSource.updateMessage(chatId.raw, messageId.raw) {
            it.copy(reaction = reaction, reactionSynced = false)
        }

        try {
            remoteMessageDataSource.sendReaction(chatId.raw, messageId.raw, reaction.toString())

            localMessageDataSource.updateMessage(chatId.raw, messageId.raw) {
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

            localMessageDataSource.updateMessage(
                messageDto.chatId,
                messageDto.id!!
            ) { updatedMessageDto }
        } catch (e: Throwable) {
            // TODO: retry sending
            localMessageDataSource.updateMessage(messageDto.chatId, messageDto.id!!) {
                it.copy(messageStatus = MessageStatus.FailedToSend)
            }
        }
    }

    private suspend fun MessageDto.toDomain(): Message {
        val parentMessage = parentMessageId?.let {
            val parentMessageDto = localMessageDataSource.message(chatId, it)
            ParentMessage(
                id = MessageId(parentMessageDto.id!!),
                direction = direction(parentMessageDto.authorId),
                content = parentMessageDto.content
            )
        }

        return Message(
            id = MessageId(id!!),
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
