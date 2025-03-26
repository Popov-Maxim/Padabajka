package com.padabajka.dating.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.event.raisedIfNotNull
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.feature.messenger.domain.ReactToMessageUseCase
import com.padabajka.dating.feature.messenger.domain.ReadMessageUseCase
import com.padabajka.dating.feature.messenger.domain.SendMessageUseCase
import com.padabajka.dating.feature.messenger.presentation.model.ConsumeInternalErrorEvent
import com.padabajka.dating.feature.messenger.presentation.model.InternalError
import com.padabajka.dating.feature.messenger.presentation.model.MessageGotReadEvent
import com.padabajka.dating.feature.messenger.presentation.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.model.MessengerState
import com.padabajka.dating.feature.messenger.presentation.model.NextMessageTextUpdateEvent
import com.padabajka.dating.feature.messenger.presentation.model.ReactToMessageEvent
import com.padabajka.dating.feature.messenger.presentation.model.RemoveParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.model.SelectParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.model.SendMessageClickEvent

class MessengerComponent(
    private val chatId: ChatId,
    private val sendMessageUseCase: Factory<SendMessageUseCase>,
    private val readMessageUseCase: Factory<ReadMessageUseCase>,
    private val reactToMessageUseCase: Factory<ReactToMessageUseCase>,
    context: ComponentContext
) : BaseComponent<MessengerState>(context, MessengerState()) {

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
            readMessageUseCase.get().invoke(chatId, messageId)
        },
        mapper = { InternalError },
        update = { state, internalError ->
            state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
        }
    )

    private fun reactToMessage(messageId: MessageId, reaction: MessageReaction) =
        mapAndReduceException(
            action = {
                reactToMessageUseCase.get().invoke(chatId, messageId, reaction)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun sendMessage(state: MessengerState) = mapAndReduceException(
        action = {
            val messageText = state.nextMessageText
            val parentMessageId = state.parentMessageId

            sendMessageUseCase.get().invoke(chatId, messageText, parentMessageId)
        },
        mapper = {
            // TODO: Implement
        },
        update = { state, _ ->
            state
        }
    )
}
