package com.padabajka.dating.feature.push.data.data.network

import kotlinx.serialization.Serializable

interface TokenApi {
    suspend fun post(data: BodyData)

    @Serializable
    data class BodyData(
        val token: String
    )
    companion object {
        const val PATH = "new_token"
    }
}
