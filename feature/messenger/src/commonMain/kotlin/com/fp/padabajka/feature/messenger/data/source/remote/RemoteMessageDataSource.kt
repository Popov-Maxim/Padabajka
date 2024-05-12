package com.fp.padabajka.feature.messenger.data.source.remote

import com.fp.padabajka.feature.messenger.data.model.MessageDto

interface RemoteMessageDataSource {
    suspend fun sendMessage(chatId: String, content: String): MessageDto
    suspend fun sendReaction(chatId: String, messageId: Long, reaction: String)
    suspend fun readMessages(chatId: String, messageId: Long)
}
