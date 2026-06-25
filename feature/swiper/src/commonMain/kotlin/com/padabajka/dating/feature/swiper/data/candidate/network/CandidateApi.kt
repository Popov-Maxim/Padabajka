package com.padabajka.dating.feature.swiper.data.candidate.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

interface CandidateApi {
    /**
     * GET /candidates
     *      ?from_age={ageRange.start}&to_age={ageRange.endInclusive}
     *          &looking_genders={lookingGenders}&distanceInKm={distanceInKm}
     *          &count={count}&excluded={loaded}
     */
    suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonResponse>

    companion object {
        const val PATH = "candidates"
    }
}
