package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

interface PersonRepository {
    suspend fun getPerson(searchPreferences: SearchPreferences): Person?
    suspend fun setUsed(personId: PersonId)
}
