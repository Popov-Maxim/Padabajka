package com.fp.padabajka.feature.profile.data.source

import androidx.datastore.core.DataStore
import com.fp.padabajka.core.presentation.isDebugBuild
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.ProfileIsNullException
import kotlinx.coroutines.flow.Flow

class DataStoreLocalEditProfileDataSource(
    private val dataStore: DataStore<Profile?>
) : LocalEditProfileDataSource {
    override val profile: Flow<Profile?>
        get() = dataStore.data

    override suspend fun replace(profile: Profile?) {
        dataStore.updateData { profile }
    }

    override suspend fun update(action: (Profile) -> Profile) {
        dataStore.updateData { profile ->
            if (profile == null) {
                if (isDebugBuild()) {
                    throw ProfileIsNullException
                }
                return@updateData profile
            }

            action(profile)
        }
    }
}
