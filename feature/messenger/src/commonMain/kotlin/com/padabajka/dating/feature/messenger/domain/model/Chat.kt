package com.padabajka.dating.feature.messenger.domain.model

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message

data class Chat(
    val id: ChatId,
    val lastMessage: Message?,
    val unreadMessagesCount: Int
)
