package com.padabajka.dating.core.repository.api.model.messenger

data class Chat(
    val id: ChatId,
    val lastEventNumber: Long
)
