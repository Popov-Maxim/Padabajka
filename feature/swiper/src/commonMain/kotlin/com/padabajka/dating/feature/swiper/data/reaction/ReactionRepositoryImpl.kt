package com.padabajka.dating.feature.swiper.data.reaction

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.source.LocalReactionDataSource
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ReactionRepositoryImpl(
    scope: CoroutineScope,
    private val remoteReactionDataSource: RemoteReactionDataSource,
    private val localReactionDataSource: LocalReactionDataSource,
    private val authRepository: AuthRepository
) : ReactionRepository {

    init {
        scope.launch {
            authRepository.authState.collect {
                if (it.userIdOrNull() == null) {
                    localReactionDataSource.clear()
                }
            }
        }
    }

    override val reactionsToMe: Flow<List<PersonReaction>>
        get() = localReactionDataSource.reactionsToMe

    override suspend fun react(reaction: PersonReaction) {
        remoteReactionDataSource.sendReaction(reaction) // TODO: add local data source
    }

    override suspend fun syncReactionsToMe() {
        val reactionsToMe = remoteReactionDataSource.reactionsToMe()
        localReactionDataSource.addReactionsToMe(reactionsToMe)
    }

    override suspend fun addReactionsToMe(reactions: PersonReaction) {
        localReactionDataSource.addReactionsToMe(listOf(reactions))
    }
}
