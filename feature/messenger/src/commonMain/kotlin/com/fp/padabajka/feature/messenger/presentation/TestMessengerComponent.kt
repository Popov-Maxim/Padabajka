package com.fp.padabajka.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.replaced
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.feature.messenger.presentation.model.ChatLoadingState
import com.fp.padabajka.feature.messenger.presentation.model.ConsumeInternalErrorEvent
import com.fp.padabajka.feature.messenger.presentation.model.EndOfMessagesListReachedEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessageGotReadEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerState
import com.fp.padabajka.feature.messenger.presentation.model.NextMessageTextUpdateEvent
import com.fp.padabajka.feature.messenger.presentation.model.ReactToMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.RemoveParentMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.SelectParentMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.SendMessageClickEvent
import com.fp.padabajka.feature.messenger.presentation.model.item.IncomingMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.MessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.OutgoingMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.addDateItems
import com.fp.padabajka.feature.messenger.presentation.util.currentDateTime
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class TestMessengerComponent(
    context: ComponentContext
) : BaseComponent<MessengerState>(context, MessengerState()) {

    private var idCounter = 1L
    private var messages = mutableListOf<MessageItem>()

    init {
        loadMoreMessages()
        componentScope.launch {
            while (true) {
                delay(1000)
                reduce {
                    messages.add(0, getMessage(false))
                    it.copy(items = messages.addDateItems())
                }
            }
        }
//        componentScope.launch {
//            while (true) {
//                delay(1000)
//                reduce {
//                    it.copy(nextMessageText = it.nextMessageText + "f")
//                }
//            }
//        }
    }

    // TODO Add message editing events
    fun onEvent(event: MessengerEvent) {
        when (event) {
            is NextMessageTextUpdateEvent -> updateNextMessageText(event.nextMessageText)
            is SelectParentMessageEvent -> updateParentMessageId(event.messageId)
            RemoveParentMessageEvent -> updateParentMessageId(null)
            ConsumeInternalErrorEvent -> consumeInternalErrorEvent()
            is MessageGotReadEvent -> readMessage(event.messageId)
            is ReactToMessageEvent -> reactToMessage(event.messageId, event.reaction)
            SendMessageClickEvent -> sendMessage(state.value)
            EndOfMessagesListReachedEvent -> loadMoreMessages()
        }
    }

    private fun updateNextMessageText(text: String) = reduce {
        it.copy(nextMessageText = text)
    }

    private fun updateParentMessageId(messageId: MessageId?) = reduce {
        it.copy(parentMessageId = messageId)
    }

    private fun consumeInternalErrorEvent() = reduce {
        it.copy(internalErrorStateEvent = consumed)
    }

    private fun readMessage(messageId: MessageId) = updateMessage(messageId) {
        if (it !is IncomingMessageItem) {
            throw IllegalStateException("Reading my own messages")
        }
        it.copy(hasBeenRead = true)
    }

    private fun reactToMessage(messageId: MessageId, reaction: MessageReaction) = updateMessage(
        messageId
    ) {
        when (it) {
            is IncomingMessageItem -> it.copy(reaction = reaction)
            is OutgoingMessageItem -> it.copy(reaction = reaction)
        }
    }

    private fun sendMessage(state: MessengerState) = reduce {
        it
    }

    private fun loadMoreMessages() = componentScope.launch {
        /*reduce {
            it.copy(chatLoadingState = ChatLoadingState.Loading)
        }
        delay(5.seconds)
        reduce {
            messages.addAll((0..20).map { getMessage() })
            it.copy(
                chatLoadingState = ChatLoadingState.Loaded,
                items = messages.addDateItems()
            )
        }*/
    }

    private fun updateMessage(messageId: MessageId, update: (MessageItem) -> MessageItem) = reduce {
        messages = messages.map { item ->
            if ((item as? MessageItem)?.id == messageId) {
                update(item)
            } else {
                item
            }
        }.toMutableList()
        it.copy(
            items = messages.addDateItems()
        )
    }

    private fun getMessage(hasBeenRead: Boolean = true): MessageItem {
        idCounter++
        return if (Random(idCounter).nextBoolean()) {
            IncomingMessageItem(
                id = MessageId(idCounter),
                content = "Incoming message",
                sentTime = currentDateTime(),
                hasBeenRead = hasBeenRead,
                reaction = null,
                parentMessage = null
            )
        } else {
            OutgoingMessageItem(
                id = MessageId(idCounter),
                content = "Outgoing message",
                sentTime = currentDateTime(),
                hasBeenRead = hasBeenRead,
                reaction = null,
                parentMessage = null
            )
        }
    }
}
