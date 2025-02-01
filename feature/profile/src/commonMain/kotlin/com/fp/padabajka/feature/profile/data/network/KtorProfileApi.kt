package com.fp.padabajka.feature.profile.data.network

import com.fp.padabajka.core.data.network.model.PersonResponse
import com.fp.padabajka.core.domain.AppSettings
import com.fp.padabajka.core.networking.KtorClientProvider
import com.fp.padabajka.core.networking.NetworkConstants
import com.fp.padabajka.core.networking.utils.appendNotNull
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path

// TODO(network): need to separate?
class KtorProfileApi(
    private val ktorClientProvider: KtorClientProvider,
    private val appSettings: AppSettings
) : ProfileApi {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun get(): PersonResponse {
        val client = ktorClientProvider.client()

        val response = try {
            client.get {
                url {
                    protocol = URLProtocol.HTTP
                    host = appSettings.host ?: NetworkConstants.DOMAIN_NAME
                    port = NetworkConstants.PORT
                    path(ProfileApi.PATH_FOR_GET)
                }
            }
        } catch (e: Exception) {
            println("Response exception: $e")
            throw e
        }

        println("Response my profile: ${response.bodyAsText()}")
        return response.body()
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun patch(
        firstName: String?,
        lastName: String?,
        birthday: String?,
        aboutMe: String?
    ) {
        val client = ktorClientProvider.client()

        val response = try {
            client.patch {
                url {
                    protocol = URLProtocol.HTTP
                    host = appSettings.host ?: NetworkConstants.DOMAIN_NAME
                    port = NetworkConstants.PORT
                    path(ProfileApi.PATH_FOR_PATCH)

                    parameters.apply {
                        appendNotNull(FIRST_NAME, firstName)
                        appendNotNull(LAST_NAME, lastName)
                        appendNotNull(BIRTHDAY, birthday)
                        appendNotNull(ABOUT_ME, aboutMe)
                    }
                }
            }
        } catch (e: Exception) {
            println("Response exception: $e")
            throw e
        }

        println("Response my profile: ${response.bodyAsText()}")
        return response.body()
    }

    companion object {
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val BIRTHDAY = "birthday"
        private const val ABOUT_ME = "about_me"
    }
}
