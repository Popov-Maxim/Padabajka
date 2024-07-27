package com.fp.padabajka.feature.auth.data

import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.core.repository.api.model.auth.AuthState
import com.fp.padabajka.core.repository.api.model.auth.LoggedIn
import com.fp.padabajka.core.repository.api.model.auth.LoggedOut
import com.fp.padabajka.core.repository.api.model.auth.UserId
import com.fp.padabajka.feature.auth.data.model.UserDto
import com.fp.padabajka.feature.auth.data.remote.RemoteAuthDataSource
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

    private fun UserDto?.toAuthState(): AuthState {
        return when {
            this == null -> LoggedOut
//            userDto.email != null && userDto.isEmailVerified.not() ->
//                WaitingForEmailValidation(userDto.id.let(::UserId)) TODO: Implement email verification
            else -> LoggedIn(this.id.let(::UserId))
        }
    }
}
