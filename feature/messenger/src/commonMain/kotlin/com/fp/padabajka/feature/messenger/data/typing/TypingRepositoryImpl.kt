package com.fp.padabajka.feature.messenger.data.typing

import com.fp.padabajka.core.repository.api.TypingRepository
import com.fp.padabajka.core.repository.api.model.messenger.ChatId
import com.fp.padabajka.feature.messenger.data.typing.source.TypingRemoteDataSource

class TypingRepositoryImpl(private val typingRemoteDataSource: TypingRemoteDataSource) :
    TypingRepository {
    override suspend fun typingStarted(chatId: ChatId) {
        typingRemoteDataSource.startTyping(chatId.raw)
    }

    override suspend fun typingStopped(chatId: ChatId) {
        typingRemoteDataSource.stopTyping(chatId.raw)
    }
}
