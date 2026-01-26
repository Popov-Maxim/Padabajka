package com.padabajka.dating.core.domain.sync

import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SyncRemoteDataUseCase(
    private val matchRepository: MatchRepository,
    private val assetRepository: AssetRepository,
    private val syncChatsUseCase: SyncChatsUseCase,
    private val reactionRepository: ReactionRepository
) {
    suspend operator fun invoke() = coroutineScope { // TODO: handle error
        runCatching {
            launch {
                matchRepository.sync()
                matchRepository.matches().first().let { matches ->
                    val matchIds = matches.map { it.chatId }
                    syncChatsUseCase(matchIds)
                }
            }
            launch {
                reactionRepository.syncReactionsToMe()
            }
            launch {
                assetRepository.loadAssets()
            }
        }.onFailure {
            println("TODO: not impl for error SyncRemoteDataUseCase ${it.message}")
            it.printStackTrace()
        }
    }
}
