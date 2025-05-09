package com.padabajka.dating.core.repository.api

interface PushRepository {
    suspend fun saveToken(token: String)
}
