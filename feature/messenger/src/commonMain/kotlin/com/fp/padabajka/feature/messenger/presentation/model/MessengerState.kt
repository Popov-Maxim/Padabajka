package com.fp.padabajka.feature.messenger.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.presentation.event.StateEvent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.repository.api.model.messenger.Message
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MessengerState(
    val nextMessageText: String = "",
    val parentMessageId: MessageId? = null,
    val messages: PersistentList<Message> = persistentListOf(),
    val internalErrorStateEvent: StateEvent = consumed
) : State
