package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.data.network.model.PersonResponse
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

interface PersonApi {
    suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonResponse>

    companion object {
        const val PATH = "person"
    }
}
