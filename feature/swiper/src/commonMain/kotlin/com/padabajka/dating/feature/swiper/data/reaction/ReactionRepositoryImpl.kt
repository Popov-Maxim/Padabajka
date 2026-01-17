package com.padabajka.dating.feature.swiper.data.reaction

import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.source.LocalReactionDataSource
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSource
import kotlinx.coroutines.flow.Flow

class ReactionRepositoryImpl(
    private val remoteReactionDataSource: RemoteReactionDataSource,
    private val localReactionDataSource: LocalReactionDataSource
) : ReactionRepository {
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
