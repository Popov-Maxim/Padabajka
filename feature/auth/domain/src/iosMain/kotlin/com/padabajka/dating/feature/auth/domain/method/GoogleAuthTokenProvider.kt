package com.padabajka.dating.feature.auth.domain.method

import cocoapods.GoogleSignIn.GIDGoogleUser
import cocoapods.GoogleSignIn.GIDSignIn
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class GoogleAuthTokenProvider {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getCredentialData(): CredentialData {
        val rootVC: UIViewController =
            UIApplication.sharedApplication.keyWindow?.rootViewController
                ?: error(
                    "Cannot get root UIViewController: " +
                        "keyWindow is null or has no rootViewController"
                )

        val signIn = GIDSignIn.sharedInstance()

        return suspendCancellableCoroutine { continuation ->
            signIn.signInWithPresentingViewController(
                rootVC
            ) { user, error ->
                if (user != null) {
                    runCatching {
                        val credentialData = user.user.toCredentialData()
                        continuation.resume(credentialData)
                    }.onFailure {
                        continuation.resumeWithException(it)
                    }
                } else {
                    val mappedError = error?.let { mapGoogleSignInError(it) }
                        ?: AuthCredentialError.Unknown()
                    continuation.resumeWithException(mappedError)
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun GIDGoogleUser.toCredentialData(): CredentialData {
        val idToken = this.idToken?.tokenString
        val accessToken = this.accessToken.tokenString
        return if (idToken != null) {
            CredentialData(idToken, accessToken)
        } else {
            error("Google ID token is missing in GIDGoogleUser")
        }
    }

    @Suppress("MagicNumber")
    private fun mapGoogleSignInError(error: NSError): AuthCredentialError {
        return when (error.domain) {
            "com.google.GIDSignIn" -> when (error.code.toInt()) {
                -5 -> AuthCredentialError.Cancelled()

                -2 -> AuthCredentialError.NetworkError()

                -4 -> AuthCredentialError.NoCredentialAvailable()

                -3 -> AuthCredentialError.InvalidCredentials()

                -8 -> AuthCredentialError.TooManyRequests()

                else -> AuthCredentialError.Unknown(
                    cause = IllegalStateException(
                        "GoogleSignIn error: ${error.code}, message=${error.description}"
                    )
                )
            }

            else -> AuthCredentialError.Unknown(
                cause = IllegalStateException(
                    "Unknown domain: ${error.domain}, code=${error.code}"
                )
            )
        }
    }
}
