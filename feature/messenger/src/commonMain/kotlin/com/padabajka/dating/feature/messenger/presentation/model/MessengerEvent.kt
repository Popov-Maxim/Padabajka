package com.padabajka.dating.feature.messenger.presentation.model

import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction

sealed interface MessengerEvent

data class SendMessageClickEvent(val message: String, val parentMessageId: MessageId?) : MessengerEvent
data class NextMessageTextUpdateEvent(val nextMessageText: String) : MessengerEvent
data object NextMessageFieldLostFocusEvent : MessengerEvent
data class MessageGotReadEvent(val messageId: MessageId) : MessengerEvent
data class ReactToMessageEvent(val messageId: MessageId, val reaction: MessageReaction) : MessengerEvent
data class SelectParentMessageEvent(val messageId: MessageId) : MessengerEvent
data object RemoveParentMessageEvent : MessengerEvent
data object ConsumeInternalErrorEvent : MessengerEvent
data object EndOfMessagesListReachedEvent : MessengerEvent
