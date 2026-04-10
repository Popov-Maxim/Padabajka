package com.padabajka.dating.feature.swiper.data.reaction.network

interface ReactionApi {
    suspend fun postReactions(reactions: Set<ReactionDto.Request>)
    suspend fun getReactions(): Set<ReactionDto.ToMeResponse>
    suspend fun deleteReaction()

    companion object {
        const val REACTIONS_PATH = "reactions"
        const val REACTION_PATH = "reaction"
        const val PATH_GET = "reactions_to_me"
    }
}
