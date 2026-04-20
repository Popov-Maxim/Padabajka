package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import kotlinx.coroutines.flow.Flow

interface ReactionRepository {
    val reactionsToMe: Flow<List<PersonReaction>>
    suspend fun react(reaction: PersonReaction)
    suspend fun deleteReaction()

    suspend fun syncReactionsToMe()
    suspend fun addReactionsToMe(reactions: PersonReaction)
}
