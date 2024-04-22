package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

interface PersonRepository {
    suspend fun getPerson(searchPreferences: SearchPreferences): Person?
    suspend fun setUsed(personId: PersonId)
}
