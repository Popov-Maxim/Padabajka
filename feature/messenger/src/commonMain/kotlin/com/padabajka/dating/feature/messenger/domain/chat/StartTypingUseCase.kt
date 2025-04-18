package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.TypingRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId

class StartTypingUseCase(private val typingRepository: TypingRepository) {
    suspend operator fun invoke(chatId: ChatId) {
        typingRepository.typingStarted(chatId)
    }
}
