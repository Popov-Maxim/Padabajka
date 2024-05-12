package com.fp.padabajka.core.repository.api.model.messenger

data class ParentMessage(
    val id: MessageId,
    val direction: MessageDirection,
    val content: String
)
