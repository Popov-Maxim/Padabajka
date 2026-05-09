package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.chat.ChatDao
import com.padabajka.dating.component.room.chat.entry.ChatEntry
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class LocalChatDataSource(
    private val chatDao: ChatDao,
    private val localMessageDataSource: LocalMessageDataSource
) {
    fun chat(chatId: ChatId): Flow<ChatEntry> {
        return chatDao.chat(chatId.raw).filterNotNull()
    }

    suspend fun getChat(chatId: ChatId): ChatEntry? {
        return chatDao.getChat(chatId.raw)
    }

    suspend fun setChat(chat: ChatEntry) {
        return chatDao.insertOrUpdate(chat)
    }

    suspend fun updateChat(chatId: ChatId, updated: (ChatEntry) -> ChatEntry) {
        val chat = getChat(chatId) ?: TODO() // TODO(P1)
        val updatedChat = updated(chat)
        setChat(updatedChat)
    }

    suspend fun deleteChat(chatId: ChatId) {
        chatDao.deleteById(chatId.raw)
        localMessageDataSource.deleteMessagesInChat(chatId)
    }

    suspend fun deleteAllExcept(chatIds: List<ChatId>) {
        val chatsForDelete = chatDao.getChats().map { ChatId(it.id) } - chatIds

        chatsForDelete.forEach { chatId ->
            deleteChat(chatId)
        }
    }
}
