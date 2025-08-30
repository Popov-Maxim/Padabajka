package com.padabajka.dating.feature.auth.domain.method

expect class GoogleAuthTokenProvider {
    suspend fun getCredentialData(): CredentialData?
}

data class CredentialData(
    val idToken: String,
    val accessToken: String?
)
