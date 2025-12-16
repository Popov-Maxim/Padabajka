package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.component.room.chat.entry.toDB
import com.padabajka.dating.component.room.chat.entry.toDomain
import com.padabajka.dating.core.repository.api.ChatRepository
import com.padabajka.dating.core.repository.api.model.messenger.Chat
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteChatDataSource

internal class ChatRepositoryImpl(
    private val remoteChatDataSource: RemoteChatDataSource,
    private val localChatDataSource: LocalChatDataSource
) : ChatRepository {
    override suspend fun getChat(chatId: ChatId): Chat? {
        return localChatDataSource.getChat(chatId)?.toDomain()
    }

    override suspend fun setChat(
        chatId: ChatId,
        chat: Chat
    ) {
        localChatDataSource.setChat(chat.toDB())
    }

    override suspend fun updateChat(chatId: ChatId, updated: (Chat) -> Chat) {
        localChatDataSource.updateChat(chatId) {
            updated(it.toDomain()).toDB()
        }
    }

    override suspend fun deleteChat(chatId: ChatId) {
        remoteChatDataSource.deleteChat(chatId.raw)
    }

    override suspend fun deleteLocalChat(chatId: ChatId) {
        TODO("Not yet implemented")
    }
}
