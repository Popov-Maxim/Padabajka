package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId

class DeleteChatUseCase(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: ChatId) {
        messageRepository.deleteChat(chatId)
    }
}
