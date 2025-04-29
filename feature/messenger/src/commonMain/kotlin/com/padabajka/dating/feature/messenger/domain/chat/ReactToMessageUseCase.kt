package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction

class ReactToMessageUseCase(private val messageRepository: MessageRepository) {

    suspend operator fun invoke(messageId: MessageId, reactionValue: MessageReaction.Value?) {
        if (reactionValue != null) {
            messageRepository.reactToMessage(messageId, reactionValue)
        } else {
            messageRepository.removeReactToMessage(messageId)
        }
    }
}
