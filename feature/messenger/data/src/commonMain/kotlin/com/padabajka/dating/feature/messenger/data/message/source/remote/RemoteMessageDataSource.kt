package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto

internal interface RemoteMessageDataSource {
    suspend fun sendMessage(chatId: String, content: String): MessageDto
    suspend fun sendReaction(messageId: String, reaction: MessageReactionDto): MessageReactionDto
    suspend fun removeReaction(messageId: String, author: String)
    suspend fun readMessages(messageId: String)
}
