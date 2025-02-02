package com.fp.padabajka.feature.swiper.data.person.source

import com.fp.padabajka.core.data.network.model.toPerson
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.network.PersonApi

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
