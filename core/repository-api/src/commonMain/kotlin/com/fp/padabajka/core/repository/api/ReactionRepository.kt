package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.Reaction

interface ReactionRepository {
    suspend fun react(reaction: Reaction)
    // TODO: add fun force send reactions
}
