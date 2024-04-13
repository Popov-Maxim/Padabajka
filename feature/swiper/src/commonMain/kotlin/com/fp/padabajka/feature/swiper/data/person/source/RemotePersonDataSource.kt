package com.fp.padabajka.feature.swiper.data.person.source

import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

interface RemotePersonDataSource {
    suspend fun getPersons(count: Int, searchPreferences: SearchPreferences): List<Person>
}
