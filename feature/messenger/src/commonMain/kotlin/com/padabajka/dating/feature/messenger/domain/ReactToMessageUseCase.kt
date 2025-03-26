package com.padabajka.dating.feature.messenger.domain

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction

class ReactToMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: ChatId, messageId: MessageId, reaction: MessageReaction) {
        // TODO: Add error handling
        messageRepository.reactToMessage(chatId, messageId, reaction)
    }
}
