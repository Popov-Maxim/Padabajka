package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.ChatRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ObserveChatHasMoreMessagesUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: ChatId): Flow<Boolean> {
        return chatRepository.chat(chatId)
            .map { it.hasMoreOldMessages }
            .distinctUntilChanged()
    }
}
