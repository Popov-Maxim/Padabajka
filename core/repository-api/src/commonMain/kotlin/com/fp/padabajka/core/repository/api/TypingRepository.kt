package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.messenger.ChatId

interface TypingRepository {
    suspend fun typingStarted(chatId: ChatId)
    suspend fun typingStopped(chatId: ChatId)
}
