package com.padabajka.dating.feature.auth.domain.method

import cocoapods.GoogleSignIn.GIDGoogleUser
import cocoapods.GoogleSignIn.GIDSignIn
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import kotlin.coroutines.resume

actual class GoogleAuthTokenProvider {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getCredentialData(): CredentialData? {
        val rootVC: UIViewController? =
            UIApplication.sharedApplication.keyWindow?.rootViewController

        if (rootVC == null) {
            return null
        }
        val signIn = GIDSignIn.sharedInstance()

        return suspendCancellableCoroutine { continuation ->
            signIn.signInWithPresentingViewController(
                rootVC
            ) { user, error ->
                val credentialData = user?.user?.toCredentialData()
                continuation.resume(credentialData)
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun GIDGoogleUser.toCredentialData(): CredentialData? {
        val idToken = this.idToken?.tokenString
        val accessToken = this.accessToken.tokenString
        return if (idToken != null) {
            CredentialData(idToken, accessToken)
        } else {
            null
        }
    }
}
