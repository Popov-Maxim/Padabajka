package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

class EditMessageUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: ChatId, messageId: MessageId, content: String) {
        messageRepository.editMessage(chatId, messageId, content)
    }
}
