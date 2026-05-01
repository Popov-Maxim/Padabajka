package com.padabajka.dating.feature.auth.domain.method

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.feature.auth.domain.BuildConfig
import io.ktor.utils.io.CancellationException

actual class GoogleAuthTokenProvider(
    private val activity: Activity
) {
    private val credentialManager = CredentialManager.create(activity)
    actual suspend fun getCredentialData(): CredentialData {
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
                error(
                    "Credential type=${credential::class.simpleName}, " +
                        "expected GOOGLE_ID_TOKEN"
                )
            }
        }.mapException { mapError(it) }.getOrThrow()
    }

    private fun credentialData(idToken: String) = CredentialData(idToken, null)

    private fun mapError(e: Throwable): AuthCredentialError {
        return when (e) {
            is androidx.credentials.exceptions.GetCredentialCancellationException ->
                AuthCredentialError.Cancelled()

            is androidx.credentials.exceptions.NoCredentialException ->
                AuthCredentialError.NoCredentialAvailable()

            is androidx.credentials.exceptions.GetCredentialInterruptedException ->
                AuthCredentialError.NetworkError()

            is androidx.credentials.exceptions.GetCredentialProviderConfigurationException ->
                AuthCredentialError.InvalidCredentials()

            is androidx.credentials.exceptions.GetCredentialUnknownException ->
                AuthCredentialError.Unknown(e)

            else -> AuthCredentialError.Unknown(e)
        }
    }
}

inline fun <T> Result<T>.mapException(
    transform: (Throwable) -> Throwable
): Result<T> {
    return fold(
        onSuccess = { Result.success(it) },
        onFailure = {
            if (it is CancellationException) {
                Result.failure(it)
            } else {
                Result.failure(transform(it))
            }
        }
    )
}
