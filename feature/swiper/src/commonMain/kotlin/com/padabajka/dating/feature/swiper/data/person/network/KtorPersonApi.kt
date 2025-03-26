package com.padabajka.dating.feature.swiper.data.person.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ParametersBuilder
import io.ktor.http.path

class KtorPersonApi(
    private val ktorClientProvider: KtorClientProvider
) : PersonApi {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonResponse> {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(PersonApi.PATH)

                parameters.append(searchPreferences)
                parameters.append(COUNT, count.toString())
                parameters.appendAll(EXCLUDED, loaded.map { it.raw })
            }
        }

        return response.body()
    }

    private fun ParametersBuilder.append(searchPreferences: SearchPreferences) {
        val ageRange = searchPreferences.ageRange
        append(FROM_AGE, ageRange.start.raw.toString())
        append(TO_AGE, ageRange.endInclusive.raw.toString())
        appendAll(LOOKING_GENDERS, searchPreferences.lookingGenders.map { it.raw })
        append(DISTANCE_IN_KM, searchPreferences.distanceInKm.toString())
    }

    companion object {
        private const val FROM_AGE = "from_age"
        private const val TO_AGE = "to_age"
        private const val LOOKING_GENDERS = "looking_genders"
        private const val DISTANCE_IN_KM = "distanceInKm"

        private const val COUNT = "count"
        private const val EXCLUDED = "excluded"
    }
}
