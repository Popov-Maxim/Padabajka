package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

interface ReactionRepository {
    suspend fun react(reaction: PersonReaction)
    suspend fun reactionsToMe(): List<PersonReaction>
    // TODO: add fun force send reactions
}
