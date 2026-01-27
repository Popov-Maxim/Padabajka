package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.ChatApi

class RemoteChatDataSource(
    private val chatApi: ChatApi
) {
    suspend fun deleteChat(chatId: ChatId) {
        chatApi.deleteChat(chatId)
    }
}
