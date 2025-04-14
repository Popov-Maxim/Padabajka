package com.padabajka.dating.feature.messenger.presentation.chat.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEvent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessengerItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ChatState(
    val nextMessageText: String = "",
    val parentMessageId: MessageId? = null,
    val messengerItems: PersistentList<MessengerItem> = persistentListOf(),
    val chatLoadingState: ChatLoadingState = ChatLoadingState.Loading,
    val internalErrorStateEvent: StateEvent = consumed
) : State
