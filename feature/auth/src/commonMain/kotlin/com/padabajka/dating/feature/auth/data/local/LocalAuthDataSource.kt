package com.padabajka.dating.feature.auth.data.local

import androidx.datastore.core.DataStore
import com.padabajka.dating.feature.auth.data.model.AuthPreferences
import kotlinx.coroutines.flow.Flow

class LocalAuthDataSource(
    private val dataStore: DataStore<AuthPreferences>
) {
    val authPreferences: Flow<AuthPreferences>
        get() = dataStore.data

    suspend fun saveEmail(email: String) {
        dataStore.updateData { it.copy(email = email) }
    }
}
