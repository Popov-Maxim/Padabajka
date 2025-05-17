package com.padabajka.dating.feature.swiper.data.reaction.network

interface ReactionApi {
    suspend fun postReactions(reactions: Set<ReactionDto>)

    companion object {
        const val PATH = "new_reactions"
    }
}
