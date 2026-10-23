package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun observePerson(personId: PersonId): Flow<Person?>
    suspend fun getPerson(personId: PersonId): Person?
    suspend fun savePerson(person: Person)
}
