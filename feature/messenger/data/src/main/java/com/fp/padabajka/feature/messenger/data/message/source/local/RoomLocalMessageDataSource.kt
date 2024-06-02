package com.fp.padabajka.feature.messenger.data.message.source.local

import com.fp.padabajka.feature.messenger.data.message.model.MessageDto
import com.fp.padabajka.feature.messenger.data.message.room.MessageDao
import com.fp.padabajka.feature.messenger.data.message.room.MessageEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomLocalMessageDataSource(private val messageDao: MessageDao) :
    LocalMessageDataSource {

    override fun messages(chatId: String): Flow<List<MessageDto>> =
        messageDao.messagesByChatId(chatId)
            .map { entries ->
                entries.map(MessageEntry::toDto)
            }

    override suspend fun message(messageId: String): MessageDto {
        return messageDao.messageById(messageId).toDto()
    }

    override suspend fun addMessage(message: MessageDto) {
        messageDao.insertMessage(message.toEntry())
    }

    override suspend fun updateMessage(
        messageId: String,
        update: (MessageDto) -> MessageDto
    ) {
        val old = messageDao.messageById(messageId)
        val updated = update(old.toDto()).toEntry()

        if (old.id != updated.id) {
            messageDao.replaceMessage(old = old, new = updated)
        }

        messageDao.updateMessage(updated)
    }
}
