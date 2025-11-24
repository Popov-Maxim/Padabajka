package com.padabajka.dating.feature.swiper.data.reaction.network

interface ReactionApi {
    suspend fun postReactions(reactions: Set<ReactionDto>)
    suspend fun getReactions(): Set<ReactionDto>

    companion object {
        const val PATH = "new_reactions"
        const val PATH_GET = "reactions_to_me"
    }
}
