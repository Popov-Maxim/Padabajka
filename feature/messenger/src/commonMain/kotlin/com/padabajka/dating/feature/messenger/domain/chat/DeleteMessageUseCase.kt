package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

class DeleteMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: ChatId, messageId: MessageId) {
        messageRepository.deleteMessage(chatId, messageId)
    }
}
