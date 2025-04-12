package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Suppress("MagicNumber")
class FakeMessengerRepository(scope: CoroutineScope) : MessageRepository {

    private var messagesList = listOf(
        Message(
            id = MessageId("1"),
            direction = MessageDirection.INCOMING,
            content = "Hellow!",
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Read,
            reaction = null,
            parentMessage = null
        ),
        Message(
            id = MessageId("2"),
            direction = MessageDirection.OUTGOING,
            content = "Hi!",
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Read,
            reaction = null,
            parentMessage = null
        ),
        Message(
            id = MessageId("3"),
            direction = MessageDirection.INCOMING,
            content = "How are you?",
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Read,
            reaction = null,
            parentMessage = null
        ),
        Message(
            id = MessageId("4"),
            direction = MessageDirection.OUTGOING,
            content = "Fine, thanks!",
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Read,
            reaction = null,
            parentMessage = null
        )
    )

    private val messagesFlow = MutableStateFlow<List<Message>>(messagesList.reversed())
    private var nextMessageId = 5L

    init {
        scope.launch {
            flow {
                while (true) {
                    val message = createIncomingMessage()
                    messagesList = messagesList + message
                    emit(nextMessageId)
                    delay(3000)
                }
            }.collect {
                messagesFlow.emit(messagesList.reversed())
            }
        }
    }

    private fun createIncomingMessage(): Message {
        val content = "New incoming message"
        val id = MessageId("${nextMessageId++}")
        return Message(
            id = id,
            direction = MessageDirection.INCOMING,
            content = content,
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Unread,
            parentMessage = null,
            reaction = null
        )
    }

    override fun messages(chatId: ChatId): Flow<List<Message>> = messagesFlow

    override suspend fun sendMessage(chatId: ChatId, content: String, parentMessageId: MessageId?) {
        val message = Message(
            id = MessageId("${nextMessageId++}"),
            direction = MessageDirection.OUTGOING,
            content = content,
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Read,
            reaction = null,
            parentMessage = null
        )
        messagesList = messagesList + message
        messagesFlow.emit(messagesList.reversed())
    }

    override suspend fun readMessage(messageId: MessageId) {
        messagesList = messagesList.map {
            if (it.id == messageId) it.copy(status = MessageStatus.Read) else it
        }
        messagesFlow.emit(messagesList.reversed())
    }

    override suspend fun reactToMessage(
        messageId: MessageId,
        reaction: MessageReaction
    ) {
        TODO("Not yet implemented")
    }
}
