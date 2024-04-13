package com.fp.padabajka.feature.swiper.data.reaction

import com.fp.padabajka.core.repository.api.ReactionRepository
import com.fp.padabajka.core.repository.api.model.swiper.AdReaction
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.core.repository.api.model.swiper.Reaction
import com.fp.padabajka.feature.swiper.data.reaction.source.RemoteReactionDataSource

class ReactionRepositoryImpl(
    private val remoteReactionDataSource: RemoteReactionDataSource
) : ReactionRepository {

    override suspend fun react(reaction: Reaction) {
        when (reaction) {
            AdReaction -> {}
            is PersonReaction -> remoteReactionDataSource.sendReaction(reaction) // TODO: add local data source
        }
    }
}
