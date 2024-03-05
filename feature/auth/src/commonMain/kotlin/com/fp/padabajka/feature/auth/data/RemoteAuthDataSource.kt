package com.fp.padabajka.feature.auth.data

interface RemoteAuthDataSource {

    suspend fun login(email: String, password: String)
    suspend fun login(token: String)
    suspend fun logout()
}
