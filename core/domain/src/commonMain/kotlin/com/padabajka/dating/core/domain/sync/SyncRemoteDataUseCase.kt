package com.padabajka.dating.core.domain.sync

import com.padabajka.dating.core.repository.api.CityRepository
import com.padabajka.dating.core.repository.api.LanguageAssetRepository
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import kotlinx.coroutines.flow.first

class SyncRemoteDataUseCase(
    private val matchRepository: MatchRepository,
    private val messageRepository: MessageRepository,
    private val cityRepository: CityRepository,
    private val languageAssetRepository: LanguageAssetRepository
) {
    suspend operator fun invoke() {
        runCatching {
            matchRepository.sync()
            matchRepository.matches().first().onEach { match ->
                messageRepository.sync(match.chatId, null, COUNT_MESSAGE_FOR_SYNC)
            }
            cityRepository.loadCities()
            languageAssetRepository.loadAssets()
        }.onFailure {
            println("TODO: not impl for error SyncRemoteDataUseCase ${it.message}")
            it.printStackTrace()
        }
    }

    companion object {
        private const val COUNT_MESSAGE_FOR_SYNC = 20
    }
}
