package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.component.room.chat.entry.toDB
import com.padabajka.dating.component.room.chat.entry.toDomain
import com.padabajka.dating.core.repository.api.ChatRepository
import com.padabajka.dating.core.repository.api.model.messenger.Chat
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteChatDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class ChatRepositoryImpl(
    private val remoteChatDataSource: RemoteChatDataSource,
    private val localChatDataSource: LocalChatDataSource
) : ChatRepository {
    private val visibleChat = MutableStateFlow<ChatId?>(null)
    override val visibleChatId: ChatId?
        get() = visibleChat.value

    override fun setVisibleChat(chatId: ChatId) {
        visibleChat.value = chatId
    }

    override fun clearVisibleChat(chatId: ChatId) {
        visibleChat.compareAndSet(chatId, null)
    }

    override fun chat(chatId: ChatId): Flow<Chat> {
        return localChatDataSource.chat(chatId).map { it.toDomain() }
    }

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
        remoteChatDataSource.deleteChat(chatId)
    }

    override suspend fun deleteLocalChat(chatId: ChatId) {
        TODO("Not yet implemented") // TODO(P1)
    }
}
