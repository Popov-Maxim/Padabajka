package com.padabajka.dating.feature.messenger.domain

import com.padabajka.dating.core.repository.api.TypingRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId

class StopTypingUseCase(private val typingRepository: TypingRepository) {
    suspend operator fun invoke(chatId: ChatId) {
        typingRepository.typingStopped(chatId)
    }
}
