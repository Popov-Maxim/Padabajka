package com.padabajka.dating.feature.auth.data.remote

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Suppress("FunctionName")
actual suspend fun _loginWithoutPassword(
    email: String,
    actionCodeSettings: dev.gitlive.firebase.auth.ActionCodeSettings
) {
    val firebaseAuth = Firebase.auth
    val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl(actionCodeSettings.url)
        .setHandleCodeInApp(actionCodeSettings.canHandleCodeInApp)
        .setIOSBundleId(actionCodeSettings.iOSBundleId)
        .setAndroidPackageName(
            actionCodeSettings.androidPackageName!!.packageName,
            actionCodeSettings.androidPackageName!!.installIfNotAvailable,
            "1"
        )
        .run { actionCodeSettings.dynamicLinkDomain?.run(::setLinkDomain) ?: this }
        .build()
    firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings).await()
}
