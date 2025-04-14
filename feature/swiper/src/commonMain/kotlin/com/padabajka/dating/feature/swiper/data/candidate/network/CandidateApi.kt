package com.padabajka.dating.feature.swiper.data.candidate.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

interface CandidateApi {
    suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonResponse>

    companion object {
        const val PATH = "candidates"
    }
}
