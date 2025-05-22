package com.padabajka.dating.core.repository.api.metadata

interface PushRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String
}
