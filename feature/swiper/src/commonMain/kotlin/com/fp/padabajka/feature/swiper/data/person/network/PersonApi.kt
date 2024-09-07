package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.network.model.PersonDto

interface PersonApi {
    suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonDto>

    companion object {
        const val PATH = "person"
    }
}
