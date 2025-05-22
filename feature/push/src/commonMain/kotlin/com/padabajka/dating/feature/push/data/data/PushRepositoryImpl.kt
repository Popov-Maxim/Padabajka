package com.padabajka.dating.feature.push.data.data

import com.padabajka.dating.core.repository.api.metadata.PushRepository
import com.padabajka.dating.feature.push.data.data.source.RemoteDataSource
import dev.gitlive.firebase.messaging.FirebaseMessaging

class PushRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val firebaseMessaging: FirebaseMessaging
) : PushRepository {
    override suspend fun saveToken(token: String) {
        remoteDataSource.saveToken(token)
    }

    override suspend fun getToken(): String {
        return firebaseMessaging.getToken()
    }
}
