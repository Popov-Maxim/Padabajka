package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

class ReadMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(messageId: MessageId) {
        // TODO: Add error handling
        messageRepository.readMessage(messageId)
    }
}
