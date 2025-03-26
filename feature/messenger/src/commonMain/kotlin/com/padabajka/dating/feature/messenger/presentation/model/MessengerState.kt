package com.padabajka.dating.feature.messenger.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEvent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MessengerState(
    val nextMessageText: String = "",
    val parentMessageId: MessageId? = null,
    val messages: PersistentList<Message> = persistentListOf(),
    val internalErrorStateEvent: StateEvent = consumed
) : State
