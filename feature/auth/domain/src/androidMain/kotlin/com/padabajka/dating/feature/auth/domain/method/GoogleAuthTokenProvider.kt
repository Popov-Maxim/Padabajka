package com.padabajka.dating.feature.auth.domain.method

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.padabajka.dating.feature.auth.domain.BuildConfig

actual class GoogleAuthTokenProvider(
    private val activity: Activity
) {
    private val credentialManager = CredentialManager.create(activity)
    actual suspend fun getCredentialData(): CredentialData? {
        val clientId = BuildConfig.GOOGLE_CLIENT_ID
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(clientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return runCatching {
            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )
            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                credentialData(googleIdTokenCredential.idToken)
            } else {
                null
            }
        }.getOrNull()
    }

    private fun credentialData(idToken: String) = CredentialData(idToken, null)
}
