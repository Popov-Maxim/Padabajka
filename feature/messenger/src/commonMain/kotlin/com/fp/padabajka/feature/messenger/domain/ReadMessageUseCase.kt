package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.swiper.PersonId

class ReadMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(personId: PersonId, messageId: MessageId) {
        // TODO: Add error handling
        messageRepository.readMessage(personId, messageId)
    }
}
