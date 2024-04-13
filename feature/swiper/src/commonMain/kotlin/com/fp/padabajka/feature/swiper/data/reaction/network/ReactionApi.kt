package com.fp.padabajka.feature.swiper.data.reaction.network

import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction

interface ReactionApi {
    suspend fun postReactions(reactions: Set<PersonReaction>)
}
