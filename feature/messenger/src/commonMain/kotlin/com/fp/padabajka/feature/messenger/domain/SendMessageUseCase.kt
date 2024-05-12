package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.ChatId
import com.fp.padabajka.core.repository.api.model.messenger.MessageId

class SendMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(
        chatId: ChatId,
        messageText: String,
        parentMessageId: MessageId?
    ) {
        // TODO Add error handling
        messageRepository.sendMessage(chatId, messageText, parentMessageId)
    }
}
