package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.swiper.PersonId

class SendMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(
        matchId: PersonId,
        messageText: String,
        parentMessageId: MessageId?
    ) {
        // TODO Add error handling
        messageRepository.sendMessage(matchId, messageText, parentMessageId)
    }
}
