package com.fp.padabajka.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raisedIfNotNull
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
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

class MessengerComponent(
    private val personId: PersonId,
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
            readMessageUseCase.get().invoke(messageId)
        },
        mapper = { InternalError },
        update = { state, internalError ->
            state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
        }
    )

    private fun reactToMessage(messageId: MessageId, reaction: MessageReaction) =
        mapAndReduceException(
            action = {
                reactToMessageUseCase.get().invoke(messageId, reaction)
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

            sendMessageUseCase.get().invoke(personId, messageText, parentMessageId)
        },
        mapper = {
            // TODO: Implement
        },
        update = { state, _ ->
            state
        }
    )
}
