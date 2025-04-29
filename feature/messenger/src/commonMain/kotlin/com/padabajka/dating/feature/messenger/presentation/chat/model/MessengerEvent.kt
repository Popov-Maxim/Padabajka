package com.padabajka.dating.feature.messenger.presentation.chat.model

import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.ParentMessageItem

sealed interface MessengerEvent

data class SendMessageClickEvent(val message: String) :
    MessengerEvent
data class NextMessageTextUpdateEvent(val nextMessageText: String) : MessengerEvent
data object NextMessageFieldLostFocusEvent : MessengerEvent
data class MessageGotReadEvent(val messageId: MessageId) : MessengerEvent
data class ReactToMessageEvent(val messageId: MessageId, val reaction: MessageReaction.Value?) :
    MessengerEvent
data class SelectParentMessageEvent(val message: ParentMessageItem) : MessengerEvent
data object RemoveParentMessageEvent : MessengerEvent
data object ConsumeInternalErrorEvent : MessengerEvent
data object EndOfMessagesListReachedEvent : MessengerEvent

data object NavigateBackEvent : MessengerEvent
