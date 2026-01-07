package com.padabajka.dating.feature.messenger.presentation.chat

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.event.raised
import com.padabajka.dating.core.presentation.event.raisedIfNotNull
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.UserPresenceRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.feature.messenger.domain.chat.ChatMessagesUseCase
import com.padabajka.dating.feature.messenger.domain.chat.DeleteChatUseCase
import com.padabajka.dating.feature.messenger.domain.chat.DeleteMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.EditMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.ReactToMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.ReadMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.SendMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.StartTypingUseCase
import com.padabajka.dating.feature.messenger.domain.chat.StopTypingUseCase
import com.padabajka.dating.feature.messenger.presentation.chat.model.ChatLoadingState
import com.padabajka.dating.feature.messenger.presentation.chat.model.ChatState
import com.padabajka.dating.feature.messenger.presentation.chat.model.ConsumeInternalErrorEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.DeleteChatEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.DeleteMatchEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.DeleteMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.EndOfMessagesListReachedEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.Field
import com.padabajka.dating.feature.messenger.presentation.chat.model.InternalError
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessageGotReadEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NavigateBackEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NextMessageFieldLostFocusEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NextMessageTextUpdateEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.ReactToMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.RemoveParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SelectMessageForEditEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SelectParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SendMessageClickEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.OutgoingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.ParentMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.addDateItems
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.toMessageItem
import com.padabajka.dating.feature.messenger.presentation.model.MatchItem
import com.padabajka.dating.feature.messenger.presentation.model.toUI
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatComponent(
    context: ComponentContext,
    private val chatId: ChatId,
    private val matchItem: MatchItem,
    private val navigateBack: () -> Unit,
    chatMessagesUseCaseFactory: Factory<ChatMessagesUseCase>,
    sendMessageUseCaseFactory: Factory<SendMessageUseCase>,
    readMessageUseCaseFactory: Factory<ReadMessageUseCase>,
    reactToMessageUseCaseFactory: Factory<ReactToMessageUseCase>,
    startTypingUseCaseFactory: Factory<StartTypingUseCase>,
    stopTypingUseCaseFactory: Factory<StopTypingUseCase>,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val matchRepository: MatchRepository,
    private val userPresenceRepository: UserPresenceRepository,
) : BaseComponent<ChatState>(context, initChatState(matchItem, userPresenceRepository)) {

    private val chatMessagesUseCase by chatMessagesUseCaseFactory.delegate()
    private val sendMessageUseCase by sendMessageUseCaseFactory.delegate()
    private val readMessageUseCase by readMessageUseCaseFactory.delegate()
    private val reactToMessageUseCase by reactToMessageUseCaseFactory.delegate()
    private val startTypingUseCase by startTypingUseCaseFactory.delegate()
    private val stopTypingUseCase by stopTypingUseCaseFactory.delegate()

    private var typingJob: Job? = null

    init {
        subscribeOnMessages()
        subscribeUserPresence(matchItem)
    }

    @Suppress("CyclomaticComplexMethod")
    fun onEvent(event: MessengerEvent) {
        when (event) {
            is NextMessageTextUpdateEvent -> updateNextMessageText(event.nextMessageText)
            NextMessageFieldLostFocusEvent -> notifyTypingStopped()
            is SelectParentMessageEvent -> updateParentMessage(event.message)
            RemoveParentMessageEvent -> updateParentMessage(null)
            ConsumeInternalErrorEvent -> consumeInternalErrorEvent()
            is MessageGotReadEvent -> readMessage(event.messageId)
            is ReactToMessageEvent -> reactToMessage(event.messageId, event.reaction)
            EndOfMessagesListReachedEvent -> loadMoreMessages()
            is SendMessageClickEvent -> sendMessage(event.field)
            NavigateBackEvent -> navigateBack()
            is DeleteMessageEvent -> deleteMessage(event.messageId)
            is SelectMessageForEditEvent -> selectMessageForEdit(event.message)

            DeleteChatEvent -> deleteChat()
            DeleteMatchEvent -> deleteMatch()
        }
    }

    override fun onStopped() {
        notifyTypingStopped()
    }

    private fun subscribeOnMessages() {
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

    private fun subscribeUserPresence(matchItem: MatchItem) {
        mapAndReduceException(
            action = {
                userPresenceRepository.userPresenceFlow(matchItem.person.id).collect {
                    val userPresence = it.toUI()
                    reduce {
                        it.copy(userPresence = userPresence)
                    }
                }
            },
            mapper = {
                it
            },
            update = { state, _ ->
                state.copy(internalErrorStateEvent = raised)
            }
        )
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
            it.copy(field = it.field.changeContent(text))
        }
        notifyTyping()
    }

    private fun Field.changeContent(content: String): Field {
        return when (this) {
            is Field.Editor -> copy(content = content)
            is Field.NewMessage -> copy(content = content)
        }
    }

    private fun updateParentMessage(parentMessageItem: ParentMessageItem?) = reduce {
        it.copy(field = it.field.changeParentMessage(parentMessageItem))
    }

    private fun Field.changeParentMessage(parentMessageItem: ParentMessageItem?): Field {
        return when (this) {
            is Field.Editor -> copy(parentMessage = parentMessageItem)
            is Field.NewMessage -> copy(parentMessage = parentMessageItem)
        }
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

    private fun reactToMessage(messageId: MessageId, reaction: MessageReaction.Value?) =
        mapAndReduceException(
            action = {
                reactToMessageUseCase(messageId, reaction)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun deleteMessage(messageId: MessageId) =
        mapAndReduceException(
            action = {
                deleteMessageUseCase(chatId, messageId)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun deleteChat() =
        mapAndReduceException(
            action = {
                deleteChatUseCase(chatId)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun deleteMatch() =
        mapAndReduceException(
            action = {
                matchRepository.deleteMatch(matchItem.id)
            },
            mapper = { InternalError },
            update = { state, internalError ->
                state.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun selectMessageForEdit(message: OutgoingMessageItem?) = reduce {
        if (message == null) {
            it.copy(field = Field.NewMessage())
        } else {
            val field = Field.Editor(message, message.content, message.parentMessage)
            it.copy(field = field)
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun sendMessage(field: Field) {
        componentScope.launch {
            reduce { state ->
                state.copy(field = Field.NewMessage())
            }

            try {
                when (field) {
                    is Field.Editor -> editMessageUseCase(chatId, field.message.id, field.content)
                    is Field.NewMessage -> sendMessageUseCase(chatId, field.content, field.parentMessage?.id)
                }
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

        private fun initChatState(
            matchItem: MatchItem,
            userPresenceRepository: UserPresenceRepository
        ): ChatState {
            val userPresence =
                userPresenceRepository.currentUserPresence(matchItem.person.id)?.toUI()
                    ?: matchItem.userPresence
            return ChatState(matchItem.person, userPresence)
        }
    }
}
