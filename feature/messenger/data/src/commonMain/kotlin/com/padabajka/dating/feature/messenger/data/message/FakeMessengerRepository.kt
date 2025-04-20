package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.ParentMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
        val hasParentMessage = (1..100).random() > 50
        val parentMessage = messagesList.takeIf { hasParentMessage }?.random()
        return Message(
            id = id,
            direction = MessageDirection.INCOMING,
            content = content,
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Unread,
            parentMessage = parentMessage?.toParent(),
            reaction = null
        )
    }

    private fun Message.toParent(): ParentMessage {
        return ParentMessage(
            id = id,
            direction = direction,
            content = content
        )
    }

    override fun messages(chatId: ChatId): Flow<List<Message>> = messagesFlow

    override fun lastMessage(chatId: ChatId): Flow<Message?> {
        return messagesFlow.map { it.first() }
    }

    override suspend fun sendMessage(chatId: ChatId, content: String, parentMessageId: MessageId?) {
        val message = Message(
            id = MessageId("${nextMessageId++}"),
            direction = MessageDirection.OUTGOING,
            content = content,
            creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            status = MessageStatus.Read,
            reaction = null,
            parentMessage = messagesList.find { it.id == parentMessageId }?.toParent()
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
        reaction: MessageReaction?
    ) {
        messagesList = messagesList.map {
            if (it.id == messageId) it.copy(reaction = reaction) else it
        }
        messagesFlow.emit(messagesList.reversed())
    }
}
