package com.padabajka.dating.feature.auth.data

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.model.auth.AuthState
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.LoggedOut
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.WaitingForEmailValidation
import com.padabajka.dating.feature.auth.data.model.UserDto
import com.padabajka.dating.feature.auth.data.remote.RemoteAuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AuthRepositoryImpl(
    private val remoteAuthDataSource: RemoteAuthDataSource
) : AuthRepository {

    override val authState: Flow<AuthState> = remoteAuthDataSource.user.map { userDto ->
        userDto.toAuthState()
    }

    override val currentAuthState: AuthState
        get() = remoteAuthDataSource.currentUser.toAuthState()

    override suspend fun authToken(): String? {
        return remoteAuthDataSource.authToken()
    }

    override suspend fun login(token: String) {
        remoteAuthDataSource.login(token)
    }

    override suspend fun login(email: String, password: String) {
        remoteAuthDataSource.login(email, password)
    }

    override suspend fun register(email: String, password: String) {
        remoteAuthDataSource.register(email, password)
        // TODO: Add email verification
    }

    override suspend fun logout() {
        remoteAuthDataSource.logout()
    }

    override suspend fun sendEmailVerification() {
        remoteAuthDataSource.sendEmailVerification()
    }

    override suspend fun reloadUser() {
        remoteAuthDataSource.reloadUser()
    }

    private fun UserDto?.toAuthState(): AuthState {
        return when {
            this == null -> LoggedOut
            email != null && isEmailVerified.not() ->
                WaitingForEmailValidation(getUserId()) // TODO: Implement email verification
            else -> LoggedIn(getUserId())
        }
    }

    private fun UserDto.getUserId() = this.id.let(::UserId)
}
