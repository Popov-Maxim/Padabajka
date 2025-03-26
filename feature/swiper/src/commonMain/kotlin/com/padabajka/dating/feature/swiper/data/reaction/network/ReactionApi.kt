package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

interface ReactionApi {
    suspend fun postReactions(reactions: Set<PersonReaction>)
}
