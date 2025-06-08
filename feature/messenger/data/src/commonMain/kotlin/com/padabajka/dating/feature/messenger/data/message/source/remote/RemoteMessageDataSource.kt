package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.feature.messenger.data.message.model.EditMessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto
import com.padabajka.dating.feature.messenger.data.message.model.SendMessageDto

internal interface RemoteMessageDataSource {
    suspend fun sendMessage(sendMessageDto: SendMessageDto): MessageDto.Existing
    suspend fun deleteMessage(chatId: String, messageId: String)
    suspend fun editMessage(editMessageDto: EditMessageDto): MessageDto.Existing
    suspend fun sendReaction(messageId: String, reaction: MessageReactionDto): MessageReactionDto
    suspend fun removeReaction(messageId: String, author: String)
    suspend fun readMessages(messageId: String)
    suspend fun getMessages(chatId: String, beforeMessageId: String?, count: Int): List<MessageDto>
}
