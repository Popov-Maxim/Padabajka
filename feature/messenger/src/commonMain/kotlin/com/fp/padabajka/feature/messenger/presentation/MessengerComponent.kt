package com.fp.padabajka.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.delegate
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raised
import com.fp.padabajka.core.presentation.event.raisedIfNotNull
import com.fp.padabajka.core.repository.api.model.messenger.ChatId
import com.fp.padabajka.core.repository.api.model.messenger.Message
import com.fp.padabajka.core.repository.api.model.messenger.MessageDirection
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.messenger.MessageStatus
import com.fp.padabajka.core.repository.api.model.messenger.ParentMessage
import com.fp.padabajka.feature.messenger.domain.ChatMessagesUseCase
import com.fp.padabajka.feature.messenger.domain.ReactToMessageUseCase
import com.fp.padabajka.feature.messenger.domain.ReadMessageUseCase
import com.fp.padabajka.feature.messenger.domain.SendMessageUseCase
import com.fp.padabajka.feature.messenger.domain.StartTypingUseCase
import com.fp.padabajka.feature.messenger.domain.StopTypingUseCase
import com.fp.padabajka.feature.messenger.presentation.model.ChatLoadingState
import com.fp.padabajka.feature.messenger.presentation.model.ConsumeInternalErrorEvent
import com.fp.padabajka.feature.messenger.presentation.model.EndOfMessagesListReachedEvent
import com.fp.padabajka.feature.messenger.presentation.model.InternalError
import com.fp.padabajka.feature.messenger.presentation.model.MessageGotReadEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerState
import com.fp.padabajka.feature.messenger.presentation.model.NextMessageFieldLostFocusEvent
import com.fp.padabajka.feature.messenger.presentation.model.NextMessageTextUpdateEvent
import com.fp.padabajka.feature.messenger.presentation.model.ReactToMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.RemoveParentMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.SelectParentMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.SendMessageClickEvent
import com.fp.padabajka.feature.messenger.presentation.model.item.IncomingMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.MessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.OutgoingMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.ParentMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.addDateItems
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MessengerComponent(
    context: ComponentContext,
    private val chatId: ChatId,
    chatMessagesUseCaseFactory: Factory<ChatMessagesUseCase>,
    sendMessageUseCaseFactory: Factory<SendMessageUseCase>,
    readMessageUseCaseFactory: Factory<ReadMessageUseCase>,
    reactToMessageUseCaseFactory: Factory<ReactToMessageUseCase>,
    startTypingUseCaseFactory: Factory<StartTypingUseCase>,
    stopTypingUseCaseFactory: Factory<StopTypingUseCase>
) : BaseComponent<MessengerState>(context, MessengerState()) {

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
            NextMessageFieldLostFocusEvent -> notifyTypingStopped()
            is SelectParentMessageEvent -> updateParentMessageId(event.messageId)
            RemoveParentMessageEvent -> updateParentMessageId(null)
            ConsumeInternalErrorEvent -> consumeInternalErrorEvent()
            is MessageGotReadEvent -> readMessage(event.messageId)
            is ReactToMessageEvent -> reactToMessage(event.messageId, event.reaction)
            EndOfMessagesListReachedEvent -> loadMoreMessages()
            SendMessageClickEvent -> sendMessage()
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

    private fun Message.toMessageItem(): MessageItem {
        return when (direction) {
            MessageDirection.OUTGOING -> OutgoingMessageItem(
                id = id,
                content = content,
                sentTime = creationTime,
                hasBeenRead = status.hasBeenRead(),
                reaction = reaction,
                parentMessage = parentMessage?.toItem()
            )
            MessageDirection.INCOMING -> IncomingMessageItem(
                id = id,
                content = content,
                sentTime = creationTime,
                hasBeenRead = status.hasBeenRead(),
                reaction = reaction,
                parentMessage = parentMessage?.toItem()
            )
        }
    }

    private fun MessageStatus.hasBeenRead(): Boolean {
        return when (this) {
            MessageStatus.Read -> true
            else -> false
        }
    }

    private fun ParentMessage.toItem(): ParentMessageItem {
        return ParentMessageItem(
            id = id,
            content = content,
            direction = direction
        )
    }

    companion object {
        private const val TYPING_STOP_DELAY = 2000L
    }
}
