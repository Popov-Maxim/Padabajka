package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.Reaction
import com.fp.padabajka.core.repository.api.model.swiper.ReactionResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    val personChannel: Channel<Person>
    val reactionResponse: Flow<ReactionResponse>
    suspend fun react(reaction: Reaction)
}
