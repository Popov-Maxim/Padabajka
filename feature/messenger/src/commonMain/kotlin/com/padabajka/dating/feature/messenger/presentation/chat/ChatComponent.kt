package com.padabajka.dating.feature.messenger.presentation.chat

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.event.raised
import com.padabajka.dating.core.presentation.event.raisedIfNotNull
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.feature.messenger.domain.chat.ChatMessagesUseCase
import com.padabajka.dating.feature.messenger.domain.chat.ReactToMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.ReadMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.SendMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.StartTypingUseCase
import com.padabajka.dating.feature.messenger.domain.chat.StopTypingUseCase
import com.padabajka.dating.feature.messenger.presentation.chat.model.ChatLoadingState
import com.padabajka.dating.feature.messenger.presentation.chat.model.ChatState
import com.padabajka.dating.feature.messenger.presentation.chat.model.ConsumeInternalErrorEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.EndOfMessagesListReachedEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.InternalError
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessageGotReadEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NavigateBackEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NextMessageFieldLostFocusEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NextMessageTextUpdateEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.ReactToMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.RemoveParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SelectParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SendMessageClickEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.addDateItems
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.toMessageItem
import com.padabajka.dating.feature.messenger.presentation.model.PersonItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatComponent(
    context: ComponentContext,
    private val chatId: ChatId,
    personItem: PersonItem,
    private val navigateBack: () -> Unit,
    chatMessagesUseCaseFactory: Factory<ChatMessagesUseCase>,
    sendMessageUseCaseFactory: Factory<SendMessageUseCase>,
    readMessageUseCaseFactory: Factory<ReadMessageUseCase>,
    reactToMessageUseCaseFactory: Factory<ReactToMessageUseCase>,
    startTypingUseCaseFactory: Factory<StartTypingUseCase>,
    stopTypingUseCaseFactory: Factory<StopTypingUseCase>
) : BaseComponent<ChatState>(context, ChatState(personItem)) {

    private val chatMessagesUseCase by chatMessagesUseCaseFactory.delegate()
    private val sendMessageUseCase by sendMessageUseCaseFactory.delegate()
    private val readMessageUseCase by readMessageUseCaseFactory.delegate()
    private val reactToMessageUseCase by reactToMessageUseCaseFactory.delegate()
    private val startTypingUseCase by startTypingUseCaseFactory.delegate()
    private val stopTypingUseCase by stopTypingUseCaseFactory.delegate()

    private var typingJob: Job? = null

    init {
        mapAndReduceException(
            action = {
                val messagesFlow = chatMessagesUseCase(chatId)
                messagesFlow
                    .map { messages ->
                        messages.map { message -> message.toMessageItem() }
                    }.map { messageItems ->
                        messageItems.addDateItems()
                    }
                    .collect { messengerItems ->
                        reduce {
                            it.copy(
                                messengerItems = messengerItems,
                                chatLoadingState = ChatLoadingState.Loaded
                            )
                        }
                    }
            },
            mapper = {
                println("Init messengerComponent: $it")
                // TODO: Track unexpected error
                // TODO: Add option to resubscribe
            },
            update = { state, _ ->
                state.copy(internalErrorStateEvent = raised)
            }
        )
    }

    fun onEvent(event: MessengerEvent) {
        when (event) {
            is NextMessageTextUpdateEvent -> updateNextMessageText(event.nextMessageText)
            NextMessageFieldLostFocusEvent -> notifyTypingStopped()
            is SelectParentMessageEvent -> updateParentMessageId(event.messageId)
            RemoveParentMessageEvent -> updateParentMessageId(null)
            ConsumeInternalErrorEvent -> consumeInternalErrorEvent()
            is MessageGotReadEvent -> readMessage(event.messageId)
            is ReactToMessageEvent -> reactToMessage(event.messageId, event.reaction)
            EndOfMessagesListReachedEvent -> loadMoreMessages()
            is SendMessageClickEvent -> sendMessage(event.message, event.parentMessageId)
            NavigateBackEvent -> navigateBack()
        }
    }

    override fun onStopped() {
        notifyTypingStopped()
    }

    private fun notifyTypingStopped() {
        componentScope.launch {
            if (typingJob == null) {
                return@launch
            }
            typingJob?.cancel()
            typingJob = null
            stopTypingUseCase(chatId)
        }
    }

    private fun notifyTyping() {
        componentScope.launch {
            if (typingJob == null) {
                startTypingUseCase(chatId)
            }

            typingJob?.cancel()
            typingJob = componentScope.launch {
                delay(TYPING_STOP_DELAY)
                stopTypingUseCase(chatId)
                typingJob = null
            }
        }
    }

    private fun loadMoreMessages() {
        // Implement message pagination
    }

    private fun updateNextMessageText(text: String) {
        reduce {
            it.copy(nextMessageText = text)
        }
        notifyTyping()
    }

    private fun updateParentMessageId(messageId: MessageId?) = reduce {
        it.copy(parentMessageId = messageId)
    }

    private fun consumeInternalErrorEvent() = reduce {
        it.copy(internalErrorStateEvent = consumed)
    }

    private fun readMessage(messageId: MessageId) = mapAndReduceException(
        action = {
            readMessageUseCase(messageId)
        },
        mapper = { InternalError },
        update = { state, internalError ->
            state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
        }
    )

    private fun reactToMessage(messageId: MessageId, reaction: MessageReaction?) =
        mapAndReduceException(
            action = {
                reactToMessageUseCase(messageId, reaction)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun sendMessage(message: String, parentMessageId: MessageId?) {
        componentScope.launch {
            reduce { state ->
                state.copy(nextMessageText = "", parentMessageId = null)
            }

            try {
                sendMessageUseCase(chatId, message, parentMessageId)
            } catch (e: Throwable) {
//                reduce { state ->
//                    state.copy(
//                        nextMessageText = message,
//                        parentMessageId = parentMessageId,
//                        internalErrorStateEvent = raised
//                    )
//                }
            }
        }
    }

    companion object {
        private const val TYPING_STOP_DELAY = 2000L
    }
}
