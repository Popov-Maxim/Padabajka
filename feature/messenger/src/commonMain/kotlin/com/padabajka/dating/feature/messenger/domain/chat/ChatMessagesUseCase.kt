package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import kotlinx.coroutines.flow.Flow

class ChatMessagesUseCase(private val messageRepository: MessageRepository) {
    operator fun invoke(chatId: ChatId): Flow<List<Message>> {
        // TODO: Add error handling
        return messageRepository.messages(chatId)
    }
}
