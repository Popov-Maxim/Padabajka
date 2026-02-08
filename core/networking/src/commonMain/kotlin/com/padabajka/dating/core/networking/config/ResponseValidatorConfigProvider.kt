package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.repository.api.exception.UserException
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.http.HttpStatusCode

class ResponseValidatorConfigProvider : KtorConfigProvider.Static {
    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            HttpResponseValidator {
                validateResponse {
                    val status = it.status
                    when (status) {
                        // TODO: server send Gone only for other person
                        HttpStatusCode.Gone -> throw UserException.Deleted()
                        HttpStatusCode.Forbidden -> throw UserException.Banned()
                        else -> Unit
                    }
                }
            }
        }
}
