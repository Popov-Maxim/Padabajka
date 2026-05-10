package com.padabajka.dating.feature.swiper.data.reaction.source

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionDto

interface RemoteReactionDataSource {
    suspend fun sendReactions(reactions: List<ReactionDto.Request>)
    suspend fun deleteReaction()
    suspend fun reactionsToMe(): List<PersonReaction>
}
