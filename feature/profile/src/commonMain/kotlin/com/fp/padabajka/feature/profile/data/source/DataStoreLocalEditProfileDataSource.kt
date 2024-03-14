package com.fp.padabajka.feature.profile.data.source

import androidx.datastore.core.DataStore
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.ProfileIsNullException
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

// TODO Add in memory update and debounced save
class DataStoreLocalEditProfileDataSource(
    private val dataStore: DataStore<Profile?>
) : LocalEditProfileDataSource {
    override val profile: Flow<Profile?>
        get() = dataStore.data

    override suspend fun replace(profile: Profile?) {
        dataStore.updateData { profile }
    }

    @Throws(ProfileIsNullException::class, CancellationException::class)
    override suspend fun update(action: (Profile) -> Profile) {
        dataStore.updateData { profile ->
            if (profile == null) throw ProfileIsNullException

            action(profile)
        }
    }
}
