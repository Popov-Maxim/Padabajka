package com.padabajka.dating.core.domain.sync

import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope

class SyncRemoteDataUseCase(
    private val matchRepository: MatchRepository,
    private val assetRepository: AssetRepository,
    private val syncChatsUseCase: SyncChatsUseCase,
    private val reactionRepository: ReactionRepository,
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke() = supervisorScope {
        val matchSync = async {
            runCatching {
                matchRepository.sync()
                matchRepository.matches().first().let { matches ->
                    val matchIds = matches.map { it.chatId }
                    syncChatsUseCase(matchIds).awaitAll()
                }
            }
        }
        val reactToMeSync = async {
            runCatching {
                reactionRepository.syncReactionsToMe()
            }
        }
        val assetsSync = async {
            runCatching {
                assetRepository.loadAssets()
            }
        }
        val subSync = async {
            runCatching {
                subscriptionRepository.syncState()
            }
        }

        val matchResult = matchSync.await()
        val list: List<Result<*>> = buildList {
            add(matchResult)
            matchResult.getOrNull()?.let {
                addAll(it)
            }
            add(reactToMeSync.await())
            add(assetsSync.await())
            add(subSync.await())
        }
        list.forEach {
            it.getOrThrow()
        }
    }
}
