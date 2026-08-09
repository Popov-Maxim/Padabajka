package com.padabajka.dating.session

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first

class LocalUserSessionDataSource(
    private val dataStore: DataStore<UserSessionOwnerDto>
) {
    suspend fun ownerId(): String? = dataStore.data.first().userId

    suspend fun setOwnerId(userId: String?) {
        dataStore.updateData { UserSessionOwnerDto(userId) }
    }
}
