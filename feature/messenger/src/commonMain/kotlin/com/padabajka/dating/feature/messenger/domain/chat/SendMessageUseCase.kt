package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

class SendMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(
        chatId: ChatId,
        messageText: String,
        parentMessageId: MessageId?
    ) {
        // TODO Add error handling
        val trimmedMessageText = messageText.trim()
        if (trimmedMessageText.isNotBlank()) {
            messageRepository.sendMessage(chatId, trimmedMessageText, parentMessageId)
        }
    }
}
