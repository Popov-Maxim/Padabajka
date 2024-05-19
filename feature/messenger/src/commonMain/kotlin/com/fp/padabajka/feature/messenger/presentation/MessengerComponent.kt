package com.fp.padabajka.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.delegate
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raised
import com.fp.padabajka.core.presentation.event.raisedIfNotNull
import com.fp.padabajka.core.repository.api.model.messenger.ChatId
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.feature.messenger.domain.ChatMessagesUseCase
import com.fp.padabajka.feature.messenger.domain.ReactToMessageUseCase
import com.fp.padabajka.feature.messenger.domain.ReadMessageUseCase
import com.fp.padabajka.feature.messenger.domain.SendMessageUseCase
import com.fp.padabajka.feature.messenger.presentation.model.ConsumeInternalErrorEvent
import com.fp.padabajka.feature.messenger.presentation.model.InternalError
import com.fp.padabajka.feature.messenger.presentation.model.MessageGotReadEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerState
import com.fp.padabajka.feature.messenger.presentation.model.NextMessageTextUpdateEvent
import com.fp.padabajka.feature.messenger.presentation.model.ReactToMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.RemoveParentMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.SelectParentMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.SendMessageClickEvent
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class MessengerComponent(
    context: ComponentContext,
    private val chatId: ChatId,
    chatMessagesUseCaseFactory: Factory<ChatMessagesUseCase>,
    sendMessageUseCaseFactory: Factory<SendMessageUseCase>,
    readMessageUseCaseFactory: Factory<ReadMessageUseCase>,
    reactToMessageUseCaseFactory: Factory<ReactToMessageUseCase>
) : BaseComponent<MessengerState>(context, MessengerState()) {

    private val chatMessagesUseCase by chatMessagesUseCaseFactory.delegate()
    private val sendMessageUseCase by sendMessageUseCaseFactory.delegate()
    private val readMessageUseCase by readMessageUseCaseFactory.delegate()
    private val reactToMessageUseCase by reactToMessageUseCaseFactory.delegate()

    init {
        mapAndReduceException(
            action = {
                val messagesFlow = chatMessagesUseCase(chatId)
                messagesFlow.collect { messages ->
                    reduce {
                        it.copy(messages = messages.toPersistentList())
                    }
                }
            },
            mapper = {
                println(it)
                // TODO: Track unexpected error
                // TODO: Add option to resubscribe
            },
            update = { state, _ ->
                state.copy(internalErrorStateEvent = raised)
            }
        )
        componentScope.launch {
            chatMessagesUseCase(chatId)
        }
    }

    fun onEvent(event: MessengerEvent) {
        when (event) {
            is NextMessageTextUpdateEvent -> updateNextMessageText(event.nextMessageText)
            is SelectParentMessageEvent -> updateParentMessageId(event.messageId)
            RemoveParentMessageEvent -> updateParentMessageId(null)
            ConsumeInternalErrorEvent -> consumeInternalErrorEvent()
            is MessageGotReadEvent -> readMessage(event.messageId)
            is ReactToMessageEvent -> reactToMessage(event.messageId, event.reaction)
            SendMessageClickEvent -> sendMessage()
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

    private fun readMessage(messageId: MessageId) = mapAndReduceException(
        action = {
            readMessageUseCase(chatId, messageId)
        },
        mapper = { InternalError },
        update = { state, internalError ->
            state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
        }
    )

    private fun reactToMessage(messageId: MessageId, reaction: MessageReaction) =
        mapAndReduceException(
            action = {
                reactToMessageUseCase(chatId, messageId, reaction)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun sendMessage() {
        componentScope.launch {
            val messageText: String
            val parentMessageId: MessageId?

            reduceBlocking { state ->
                messageText = state.nextMessageText
                parentMessageId = state.parentMessageId

                state.copy(nextMessageText = "", parentMessageId = null)
            }

            try {
                sendMessageUseCase(chatId, messageText, parentMessageId)
            } catch (e: Throwable) {
                reduceBlocking { state ->
                    state.copy(
                        nextMessageText = messageText,
                        parentMessageId = parentMessageId,
                        internalErrorStateEvent = raised
                    )
                }
            }
        }
    }
}
