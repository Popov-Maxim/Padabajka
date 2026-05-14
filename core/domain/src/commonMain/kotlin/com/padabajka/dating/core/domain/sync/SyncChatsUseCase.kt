package com.padabajka.dating.core.domain.sync

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SyncChatsUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatIds: List<ChatId>): List<Deferred<Result<Unit>>> = coroutineScope {
        chatIds.map { chatId ->
            async {
                runCatching {
                    messageRepository.sync(chatId)
                }
            }
        }
    }
}
