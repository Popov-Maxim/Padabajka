package com.padabajka.dating.feature.swiper.data.person.source

import com.padabajka.dating.core.data.network.model.toPerson
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.data.person.network.PersonApi

class RemotePersonDataSourceImpl(
    private val personApi: PersonApi
) : RemotePersonDataSource {

    override suspend fun getPersons(
        count: Int,
        loaded: Set<Person>,
        searchPreferences: SearchPreferences
    ): List<Person> {
        return personApi.getPersons(count, loaded.map(Person::id), searchPreferences)
            .map { it.toPerson() }
    }
}
