package com.padabajka.dating.feature.messenger.presentation.chat.model

import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.OutgoingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.ParentMessageItem

sealed interface MessengerEvent

data class SendMessageClickEvent(val field: Field) :
    MessengerEvent
data class NextMessageTextUpdateEvent(val nextMessageText: String) : MessengerEvent
data object NextMessageFieldLostFocusEvent : MessengerEvent
data class MessageGotReadEvent(val messageId: MessageId) : MessengerEvent
data class ReactToMessageEvent(val messageId: MessageId) :
    MessengerEvent
data class DeleteMessageEvent(val messageId: MessageId) : MessengerEvent
data class SelectMessageForEditEvent(val message: OutgoingMessageItem?) : MessengerEvent
data class SelectParentMessageEvent(val message: ParentMessageItem) : MessengerEvent
data object RemoveParentMessageEvent : MessengerEvent
data object ConsumeInternalErrorEvent : MessengerEvent
data object EndOfMessagesListReachedEvent : MessengerEvent

data object DeleteChatEvent : MessengerEvent
data object DeleteMatchEvent : MessengerEvent

data object NavigateBackEvent : MessengerEvent
