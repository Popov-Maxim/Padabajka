package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest

internal interface RemoteMessageDataSource {
    suspend fun sendMessage(sendMessageDto: MessageRequest.Send): MessageDto.Existing
    suspend fun deleteMessage(chatId: String, messageId: String)
    suspend fun editMessage(editMessageDto: MessageRequest.Edit): MessageDto.Existing
    suspend fun sendReaction(messageId: String, reaction: MessageReactionDto): MessageReactionDto
    suspend fun removeReaction(messageId: String, author: String)
    suspend fun readMessages(messageRequest: MessageRequest.MarkAsRead)
    suspend fun getMessages(chatId: String, beforeMessageId: String?, count: Int): List<MessageDto>
}
