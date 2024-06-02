package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.TypingRepository
import com.fp.padabajka.core.repository.api.model.messenger.ChatId

class StartTypingUseCase(private val typingRepository: TypingRepository) {
    suspend operator fun invoke(chatId: ChatId) {
        typingRepository.typingStarted(chatId)
    }
}
