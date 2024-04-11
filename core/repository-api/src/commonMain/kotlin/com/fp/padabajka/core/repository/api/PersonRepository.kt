package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    val person: Flow<Person>
    suspend fun react(reaction: PersonReaction)
}
