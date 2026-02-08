package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.feature.profile.data.network.AccountApi

class RemoveAccountDataSource(
    private val accountApi: AccountApi
) {
    suspend fun delete() {
        accountApi.delete()
    }
}
