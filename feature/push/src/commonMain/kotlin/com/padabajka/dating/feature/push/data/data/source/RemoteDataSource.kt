package com.padabajka.dating.feature.push.data.data.source

interface RemoteDataSource {
    suspend fun saveToken(token: String)
}
