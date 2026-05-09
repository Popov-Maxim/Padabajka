package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.messenger.Chat
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun chat(chatId: ChatId): Flow<Chat>

    suspend fun getChat(chatId: ChatId): Chat?
    suspend fun setChat(chatId: ChatId, chat: Chat)
    suspend fun updateChat(chatId: ChatId, updated: (Chat) -> Chat)

    suspend fun deleteChat(chatId: ChatId)
    suspend fun deleteLocalChat(chatId: ChatId)
}
