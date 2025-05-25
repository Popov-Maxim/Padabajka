package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import kotlinx.coroutines.flow.Flow

internal interface LocalMessageDataSource {
    fun messages(chatId: String): Flow<List<MessageEntry>>
    fun lastMessage(chatId: String): Flow<MessageEntry?>
    suspend fun message(messageId: String): MessageEntry
    suspend fun addMessage(message: MessageEntry)
    suspend fun addMessages(messages: List<MessageEntry>)
    suspend fun updateMessage(messageId: String, update: (MessageEntry) -> MessageEntry)
}
