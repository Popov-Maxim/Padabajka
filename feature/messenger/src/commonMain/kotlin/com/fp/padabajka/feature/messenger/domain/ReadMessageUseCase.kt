package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.MessageId

class ReadMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(messageId: MessageId) {
        // TODO: Add error handling
        messageRepository.readMessage(messageId)
    }
}
