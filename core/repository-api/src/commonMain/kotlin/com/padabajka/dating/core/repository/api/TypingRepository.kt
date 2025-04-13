package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.messenger.ChatId

interface TypingRepository {
    suspend fun typingStarted(chatId: ChatId)
    suspend fun typingStopped(chatId: ChatId)
}
