package com.fp.padabajka.feature.messenger.data.message.source.remote

import com.fp.padabajka.feature.messenger.data.message.model.MessageDto

internal interface RemoteMessageDataSource {
    suspend fun sendMessage(chatId: String, content: String): MessageDto
    suspend fun sendReaction(messageId: String, reaction: String)
    suspend fun readMessages(messageId: String)
}