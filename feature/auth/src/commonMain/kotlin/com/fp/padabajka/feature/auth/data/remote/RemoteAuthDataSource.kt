package com.fp.padabajka.feature.auth.data.remote

import com.fp.padabajka.feature.auth.data.model.UserDto
import kotlinx.coroutines.flow.Flow

internal interface RemoteAuthDataSource {

    val user: Flow<UserDto?>
    suspend fun login(email: String, password: String)
    suspend fun login(token: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
}
