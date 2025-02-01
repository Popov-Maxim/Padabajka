package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.data.network.model.PersonResponse
import com.fp.padabajka.core.domain.AppSettings
import com.fp.padabajka.core.networking.KtorClientProvider
import com.fp.padabajka.core.networking.NetworkConstants
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlin.time.measureTimedValue

class KtorPersonApi(
    private val ktorClientProvider: KtorClientProvider,
    private val appSettings: AppSettings
) : PersonApi {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonResponse> {
        val (client, duration) = measureTimedValue {
            ktorClientProvider.client()
        }
        println("Client creation duration: ${duration.inWholeMilliseconds}")

        val response = try {
            client.get {
                url {
                    protocol = URLProtocol.HTTP
                    host = appSettings.host ?: NetworkConstants.DOMAIN_NAME
                    port = NetworkConstants.PORT
                    path(PersonApi.PATH)

                    parameters.append(searchPreferences)
                    parameters.append(COUNT, count.toString())
                    parameters.appendAll(EXCLUDED, loaded.map { it.raw })
                }
                println("Request: ${this.url}")
            }
        } catch (e: Exception) {
            println("Response exception: $e")
            throw e
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

        private const val COUNT = "count"
        private const val EXCLUDED = "excluded"
    }
}
