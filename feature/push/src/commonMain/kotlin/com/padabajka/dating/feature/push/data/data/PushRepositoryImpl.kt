package com.padabajka.dating.feature.push.data.data

import com.padabajka.dating.core.repository.api.PushRepository
import com.padabajka.dating.feature.push.data.data.source.RemoteDataSource

class PushRepositoryImpl(private val remoteDataSource: RemoteDataSource) : PushRepository {
    override suspend fun saveToken(token: String) {
        remoteDataSource.saveToken(token)
    }
}
