package com.padabajka.dating.feature.swiper.data.reaction

import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSource

class ReactionRepositoryImpl(
    private val remoteReactionDataSource: RemoteReactionDataSource
) : ReactionRepository {

    override suspend fun react(reaction: PersonReaction) {
        remoteReactionDataSource.sendReaction(reaction) // TODO: add local data source
    }

    override suspend fun reactionsToMe(): List<PersonReaction> {
        return remoteReactionDataSource.reactionsToMe()
    }
}
