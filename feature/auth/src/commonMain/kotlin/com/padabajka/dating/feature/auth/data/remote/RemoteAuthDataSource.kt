package com.padabajka.dating.feature.auth.data.remote

import com.padabajka.dating.feature.auth.data.model.UserDto
import dev.gitlive.firebase.auth.ActionCodeSettings
import dev.gitlive.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

internal interface RemoteAuthDataSource {

    val user: Flow<UserDto?>
    val currentUser: UserDto?
    suspend fun authToken(): String?
    suspend fun login(email: String, password: String)
    suspend fun loginWithoutPassword(email: String, actionCodeSettings: ActionCodeSettings)
    suspend fun signInWithEmailLink(email: String, link: String)
    suspend fun login(token: String)
    suspend fun loginInWithCredential(credential: AuthCredential)
    suspend fun register(email: String, password: String)
    suspend fun logout()

    suspend fun loginDebug(uuid: String)

    suspend fun sendEmailVerification()
    suspend fun reloadUser()
}
