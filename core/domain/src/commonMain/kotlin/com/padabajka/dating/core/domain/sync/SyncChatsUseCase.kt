package com.padabajka.dating.core.domain.sync

import com.padabajka.dating.core.repository.api.ChatRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.Chat
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SyncChatsUseCase(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatIds: List<ChatId>): Unit = coroutineScope {
        chatIds.map { chatId ->
            async {
                val chat = chatRepository.getChat(chatId)
                val syncResult = if (chat == null || chat.lastEventNumber < 0) {
                    messageRepository.loadMessages(chatId, COUNT_MESSAGE_FOR_SYNC)
                } else {
                    messageRepository.syncMessages(
                        chatId,
                        chat.lastEventNumber,
                        chat.lastReadEventNumber
                    )
                }
                val newLastEventNumber = syncResult.lastEventNumber
                val newLastReadEventNumber = syncResult.lastReadEventNumber
                val newChat = chat?.copy(
                    lastEventNumber = newLastEventNumber,
                    lastReadEventNumber = newLastReadEventNumber
                ) ?: Chat(
                    id = chatId,
                    lastEventNumber = newLastEventNumber,
                    lastReadEventNumber = newLastReadEventNumber
                )

                chatRepository.setChat(chatId, newChat)
            }
        }
    }

    companion object {
        private const val COUNT_MESSAGE_FOR_SYNC = 20
    }
}
