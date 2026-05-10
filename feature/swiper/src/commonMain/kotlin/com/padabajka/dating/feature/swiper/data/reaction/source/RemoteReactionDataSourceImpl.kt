package com.padabajka.dating.feature.swiper.data.reaction.source

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionApi
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionDto
import com.padabajka.dating.feature.swiper.data.reaction.network.toDomain

class RemoteReactionDataSourceImpl(
    private val reactionApi: ReactionApi
) : RemoteReactionDataSource {

    override suspend fun sendReactions(reactions: List<ReactionDto.Request>) {
        reactionApi.postReactions(reactions.toSet())
    }

    override suspend fun deleteReaction() {
        reactionApi.deleteReaction()
    }

    override suspend fun reactionsToMe(): List<PersonReaction> {
        return reactionApi.getReactions().map { it.toDomain() }
    }
}
