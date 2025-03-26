package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

class FakeReactionApi : ReactionApi {
    override suspend fun postReactions(reactions: Set<PersonReaction>) = Unit
}
