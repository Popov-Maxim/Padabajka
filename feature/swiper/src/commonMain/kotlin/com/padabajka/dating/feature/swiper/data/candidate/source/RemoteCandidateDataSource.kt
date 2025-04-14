package com.padabajka.dating.feature.swiper.data.candidate.source

import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

interface RemoteCandidateDataSource {
    suspend fun getPersons(
        count: Int,
        loaded: Set<Person>,
        searchPreferences: SearchPreferences
    ): List<Person>
}
