package com.fp.padabajka.feature.swiper.data.person.source

import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.network.PersonApi
import com.fp.padabajka.feature.swiper.data.person.network.model.toPerson

class RemotePersonDataSourceImpl(
    private val personApi: PersonApi
) : RemotePersonDataSource {

    override suspend fun getPersons(
        count: Int,
        loaded: Set<Person>,
        searchPreferences: SearchPreferences
    ): List<Person> {
        return personApi.getPersons(count, loaded.map(Person::id), searchPreferences.toConfig())
            .map { it.toPerson() }
    }

    private fun SearchPreferences.toConfig(): PersonApi.Configuration {
        return TODO()
    }
}
