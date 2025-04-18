package com.padabajka.dating.feature.profile.data.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.profile.data.ProfileIsNullException
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

// TODO Add in memory update and debounced save
class DataStoreLocalDraftProfileDataSource(
    private val dataStore: DataStore<Profile?>
) : LocalDraftProfileDataSource {
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
