package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.ChatRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId

class DeleteChatUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(chatId: ChatId) {
        chatRepository.deleteChat(chatId)
    }
}
