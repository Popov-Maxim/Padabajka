package com.padabajka.dating.feature.swiper.data.candidate.source

import com.padabajka.dating.core.data.network.model.toPerson
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.data.candidate.network.CandidateApi

class RemoteCandidateDataSourceImpl(
    private val candidateApi: CandidateApi
) : RemoteCandidateDataSource {

    override suspend fun getPersons(
        count: Int,
        loaded: Set<Person>,
        searchPreferences: SearchPreferences
    ): List<Person> {
        return candidateApi.getPersons(count, loaded.map(Person::id), searchPreferences)
            .map { it.toPerson() }
    }
}
