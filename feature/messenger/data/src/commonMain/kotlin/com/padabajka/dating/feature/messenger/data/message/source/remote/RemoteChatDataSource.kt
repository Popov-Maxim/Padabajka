package com.padabajka.dating.feature.messenger.data.message.source.remote

class RemoteChatDataSource(
    private val messageApi: MessageApi
) {
    suspend fun deleteChat(chatId: String) {
        messageApi.deleteChat(chatId)
    }
}
