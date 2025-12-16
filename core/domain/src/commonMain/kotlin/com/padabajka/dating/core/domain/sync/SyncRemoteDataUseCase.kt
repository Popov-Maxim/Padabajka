package com.padabajka.dating.core.domain.sync

import com.padabajka.dating.core.repository.api.CityRepository
import com.padabajka.dating.core.repository.api.LanguageAssetRepository
import com.padabajka.dating.core.repository.api.MatchRepository
import kotlinx.coroutines.flow.first

class SyncRemoteDataUseCase(
    private val matchRepository: MatchRepository,
    private val cityRepository: CityRepository,
    private val languageAssetRepository: LanguageAssetRepository,
    private val syncChatsUseCase: SyncChatsUseCase
) {
    suspend operator fun invoke() {
        runCatching {
            matchRepository.sync()
            matchRepository.matches().first().let { matches ->
                val matchIds = matches.map { it.chatId }
                syncChatsUseCase(matchIds)
            }
            cityRepository.loadCities()
            languageAssetRepository.loadAssets()
        }.onFailure {
            println("TODO: not impl for error SyncRemoteDataUseCase ${it.message}")
            it.printStackTrace()
        }
    }
}
