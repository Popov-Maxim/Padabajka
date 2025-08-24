package com.padabajka.dating.feature.auth.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

@Suppress("FunctionName")
actual suspend fun _loginWithoutPassword(
    email: String,
    actionCodeSettings: dev.gitlive.firebase.auth.ActionCodeSettings
) {
    Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
}
