package com.padabajka.dating.feature.swiper.data.person.source

import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

interface RemotePersonDataSource {
    suspend fun getPersons(
        count: Int,
        loaded: Set<Person>,
        searchPreferences: SearchPreferences
    ): List<Person>
}
