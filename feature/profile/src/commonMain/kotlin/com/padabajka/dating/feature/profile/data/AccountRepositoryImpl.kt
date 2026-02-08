package com.padabajka.dating.feature.profile.data

import com.padabajka.dating.core.repository.api.AccountRepository
import com.padabajka.dating.feature.profile.data.source.RemoveAccountDataSource

class AccountRepositoryImpl(
    private val removeAccountDataSource: RemoveAccountDataSource
) : AccountRepository {
    override suspend fun delete() {
        removeAccountDataSource.delete()
    }
}
