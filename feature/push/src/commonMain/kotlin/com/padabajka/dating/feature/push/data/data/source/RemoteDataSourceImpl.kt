package com.padabajka.dating.feature.push.data.data.source

import com.padabajka.dating.feature.push.data.data.network.TokenApi

class RemoteDataSourceImpl(private val api: TokenApi) : RemoteDataSource {
    override suspend fun saveToken(token: String) {
        api.post(TokenApi.BodyData(token))
    }
}
