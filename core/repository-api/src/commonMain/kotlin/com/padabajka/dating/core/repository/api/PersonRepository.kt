package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

interface PersonRepository {
    suspend fun getPerson(personId: PersonId): Person?
    suspend fun savePerson(person: Person)
}
