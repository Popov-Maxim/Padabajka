package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction

class ReactToMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(messageId: MessageId, reaction: MessageReaction) {
        // TODO: Add error handling
        messageRepository.reactToMessage(messageId, reaction)
    }
}
