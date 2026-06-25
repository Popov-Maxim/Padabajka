package com.padabajka.dating.feature.swiper.data.reaction.network

interface ReactionApi {

    /**
     * POST /reactions
     */
    suspend fun postReactions(reactions: Set<ReactionDto.Request>)

    /**
     * GET /reactions_to_me
     */
    suspend fun getReactions(): Set<ReactionDto.ToMeResponse>

    /**
     * DELETE /reaction
     */
    suspend fun deleteReaction()

    companion object {
        const val REACTIONS_PATH = "reactions"
        const val REACTION_PATH = "reaction"
        const val PATH_GET = "reactions_to_me"
    }
}
