package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class KtorReactionApi(
    private val ktorClientProvider: KtorClientProvider
) : ReactionApi {
    override suspend fun postReactions(reactions: Set<ReactionDto.Request>) {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(ReactionApi.REACTIONS_PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(reactions)
        }

        return response.throwIfNotSuccessful()
    }

    override suspend fun getReactions(): Set<ReactionDto.ToMeResponse> {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(ReactionApi.PATH_GET)
            }
        }

        return response.body()
    }

    override suspend fun deleteReaction() {
        val client = ktorClientProvider.client()

        val response = client.delete {
            url {
                path(ReactionApi.REACTION_PATH)
            }
        }

        return response.throwIfNotSuccessful()
    }
}
