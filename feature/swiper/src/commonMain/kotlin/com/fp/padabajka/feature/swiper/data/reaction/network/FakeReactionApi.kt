package com.fp.padabajka.feature.swiper.data.reaction.network

import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction

class FakeReactionApi : ReactionApi {
    override suspend fun postReactions(reactions: Set<PersonReaction>) = Unit
}
