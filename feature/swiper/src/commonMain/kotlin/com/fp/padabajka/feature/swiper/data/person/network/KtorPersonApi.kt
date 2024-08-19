package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.networking.KtorClientProvider
import com.fp.padabajka.core.networking.NetworkConstants
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.network.model.PersonDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.path

class KtorPersonApi(
    private val ktorClientProvider: KtorClientProvider
) : PersonApi {
    override suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonDto> {
        val client = ktorClientProvider.client()
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = NetworkConstants.DOMAIN_NAME
                port = NetworkConstants.PORT
                path(PersonApi.PATH)

                parameters.append(searchPreferences)
            }
        }

        println("Response: ${response.bodyAsText()}")
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
    }
}
