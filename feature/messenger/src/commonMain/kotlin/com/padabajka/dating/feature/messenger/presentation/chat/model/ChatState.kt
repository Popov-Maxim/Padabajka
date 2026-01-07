package com.padabajka.dating.feature.messenger.presentation.chat.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEvent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessengerItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.OutgoingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.ParentMessageItem
import com.padabajka.dating.feature.messenger.presentation.model.PersonItem
import com.padabajka.dating.feature.messenger.presentation.model.UserPresenceItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ChatState(
    val person: PersonItem,
    val userPresence: UserPresenceItem,
    val field: Field = Field.NewMessage(),
    val messengerItems: PersistentList<MessengerItem> = persistentListOf(),
    val chatLoadingState: ChatLoadingState = ChatLoadingState.Loading,
    val internalErrorStateEvent: StateEvent = consumed
) : State

sealed interface Field {
    val content: String
    val parentMessage: ParentMessageItem?

    data class NewMessage(
        override val content: String = "",
        override val parentMessage: ParentMessageItem? = null,
    ) : Field
    data class Editor(
        val message: OutgoingMessageItem,
        override val content: String,
        override val parentMessage: ParentMessageItem?,
    ) : Field
}
