package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.data.message.model.ChatRequest
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.MessageReadEventApi

class RemoteChatReadStateDataSource(
    private val messageReadEventApi: MessageReadEventApi
) {
    suspend fun sendReadState(chatId: ChatId, request: ChatRequest.MarkAsRead) {
        messageReadEventApi.markChatAsRead(chatId, request)
    }
}
