package com.fp.padabajka.feature.messenger.presentation.model.item

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.repository.api.model.messenger.MessageDirection
import com.fp.padabajka.core.repository.api.model.messenger.MessageId

@Immutable
data class ParentMessageItem(
    val id: MessageId,
    val content: String,
    val direction: MessageDirection
)
