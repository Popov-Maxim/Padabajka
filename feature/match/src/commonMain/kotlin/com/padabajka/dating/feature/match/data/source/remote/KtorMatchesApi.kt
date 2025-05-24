package com.padabajka.dating.feature.match.data.source.remote

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.feature.match.data.model.MatchDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

class KtorMatchesApi(
    private val ktorClientProvider: KtorClientProvider
) : MatchesApi {
    override suspend fun getMatches(): List<MatchDto> {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(MatchesApi.PATH)
            }
        }

        return response.body()
    }
}
