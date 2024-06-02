package com.fp.padabajka.feature.messenger.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.presentation.event.StateEvent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.feature.messenger.presentation.model.item.MessengerItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MessengerState(
    val nextMessageText: String = "",
    val parentMessageId: MessageId? = null,
    val messengerItems: PersistentList<MessengerItem> = persistentListOf(),
    val chatLoadingState: ChatLoadingState = ChatLoadingState.Loading,
    val internalErrorStateEvent: StateEvent = consumed
) : State
