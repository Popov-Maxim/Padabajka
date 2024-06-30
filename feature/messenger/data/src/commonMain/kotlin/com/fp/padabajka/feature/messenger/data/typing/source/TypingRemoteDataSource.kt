package com.fp.padabajka.feature.messenger.data.typing.source

interface TypingRemoteDataSource {
    suspend fun startTyping(chatId: String)
    suspend fun stopTyping(chatId: String)
}
