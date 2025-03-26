package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.Reaction

interface ReactionRepository {
    suspend fun react(reaction: Reaction)
    // TODO: add fun force send reactions
}
