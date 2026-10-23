package com.padabajka.dating.core.repository.api

interface AuthSessionHandler {
    suspend fun onUnauthorized()
}
