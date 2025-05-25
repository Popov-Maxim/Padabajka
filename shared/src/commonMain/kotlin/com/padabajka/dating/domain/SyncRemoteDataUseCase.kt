package com.padabajka.dating.domain

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import kotlinx.coroutines.flow.first

class SyncRemoteDataUseCase(
    private val matchRepository: MatchRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke() {
        matchRepository.sync()
        matchRepository.matches().first().onEach { match ->
            messageRepository.sync(match.chatId, null, COUNT_MESSAGE_FOR_SYNC)
        }
    }

    companion object {
        private const val COUNT_MESSAGE_FOR_SYNC = 20
    }
}
