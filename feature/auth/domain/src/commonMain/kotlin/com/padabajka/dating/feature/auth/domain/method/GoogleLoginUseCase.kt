package com.padabajka.dating.feature.auth.domain.method

import com.padabajka.dating.core.repository.api.AuthRepository
import dev.gitlive.firebase.auth.GoogleAuthProvider

class GoogleLoginUseCase(
    private val googleAuthProvider: GoogleAuthTokenProvider,
    private val authRepository: AuthRepository
) {
    suspend fun login() {
        val credentialData = googleAuthProvider.getCredentialData()
        val credential = GoogleAuthProvider.credential(credentialData?.idToken, credentialData?.accessToken)
        authRepository.loginInWithCredential(credential)
    }
}
