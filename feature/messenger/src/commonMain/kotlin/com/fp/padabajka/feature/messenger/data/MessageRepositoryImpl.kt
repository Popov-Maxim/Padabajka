package com.fp.padabajka.feature.messenger.data

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.Message
import com.fp.padabajka.core.repository.api.model.messenger.MessageDirection
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.messenger.MessageStatus
import com.fp.padabajka.core.repository.api.model.messenger.ParentMessage
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.feature.messenger.data.model.MessageDto
import com.fp.padabajka.feature.messenger.data.source.local.LocalMessageDataSource
import com.fp.padabajka.feature.messenger.data.source.remote.RemoteMessageDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MessageRepositoryImpl(
    private val localMessageDataSource: LocalMessageDataSource,
    private val remoteMessageDataSource: RemoteMessageDataSource
) : MessageRepository {

    override fun messages(matchId: PersonId): Flow<List<Message>> =
        localMessageDataSource.messages(matchId.raw).map { messages ->
            messages.map { it.toDomain() }
        }

    override suspend fun sendMessage(
        matchId: PersonId,
        content: String,
        parentMessageId: MessageId?
    ) {
        val currentTime = nowMilliseconds()

        val messageDto = localMessageDataSource.addMessage(
            MessageDto(
                id = null,
                matchId = matchId.raw,
                isIncoming = false,
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
    override suspend fun readMessage(matchId: PersonId, messageId: MessageId) {
        localMessageDataSource.updateMessage(matchId.raw, messageId.raw) {
            it.copy(messageStatus = MessageStatus.Read, readSynced = false)
        }

        try {
            remoteMessageDataSource.readMessages(matchId.raw, messageId.raw)

            localMessageDataSource.updateMessage(matchId.raw, messageId.raw) {
                it.copy(readSynced = true)
            }
        } catch (e: Throwable) {
            // TODO: retry read updating
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun reactToMessage(
        matchId: PersonId,
        messageId: MessageId,
        reaction: MessageReaction
    ) {
        localMessageDataSource.updateMessage(matchId.raw, messageId.raw) {
            it.copy(reaction = reaction, reactionSynced = false)
        }

        try {
            remoteMessageDataSource.sendReaction(matchId.raw, messageId.raw, reaction.toString())

            localMessageDataSource.updateMessage(matchId.raw, messageId.raw) {
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
                remoteMessageDataSource.sendMessage(messageDto.matchId, messageDto.content)

            localMessageDataSource.updateMessage(
                messageDto.matchId,
                messageDto.id!!
            ) { updatedMessageDto }
        } catch (e: Throwable) {
            // TODO: retry sending
            localMessageDataSource.updateMessage(messageDto.matchId, messageDto.id!!) {
                it.copy(messageStatus = MessageStatus.FailedToSend)
            }
        }
    }

    private suspend fun MessageDto.toDomain(): Message {
        val parentMessage = parentMessageId?.let {
            val parentMessageDto = localMessageDataSource.message(matchId, it)
            ParentMessage(
                id = MessageId(parentMessageDto.id!!),
                direction = direction(parentMessageDto.isIncoming),
                content = parentMessageDto.content
            )
        }

        return Message(
            id = MessageId(id!!),
            direction = direction(isIncoming),
            content = content,
            creationTime = localDateTime(creationTime),
            status = messageStatus,
            reaction = reaction,
            parentMessage = parentMessage
        )
    }

    private fun direction(isIncoming: Boolean): MessageDirection {
        return if (isIncoming) {
            MessageDirection.INCOMING
        } else {
            MessageDirection.OUTGOING
        }
    }

    private fun nowMilliseconds(): Long = Clock.System.now().toEpochMilliseconds()

    private fun localDateTime(millis: Long): LocalDateTime =
        Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
}
