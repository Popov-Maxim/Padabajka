package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.swiper.PersonId

class ReactToMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(matchId: PersonId, messageId: MessageId, reaction: MessageReaction) {
        // TODO: Add error handling
        messageRepository.reactToMessage(matchId, messageId, reaction)
    }
}
