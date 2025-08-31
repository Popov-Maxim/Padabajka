package com.padabajka.dating.feature.profile.data.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import kotlinx.coroutines.flow.Flow

// TODO Add in memory update and debounced save
class DataStoreLocalDraftProfileDataSource(
    private val dataStore: DataStore<DraftProfile>
) : LocalDraftProfileDataSource {
    override val profile: Flow<DraftProfile>
        get() = dataStore.data

    override suspend fun replace(profile: DraftProfile) {
        dataStore.updateData { profile }
    }

    override suspend fun update(action: (DraftProfile) -> DraftProfile) {
        dataStore.updateData { profile ->

            action(profile)
        }
    }
}
