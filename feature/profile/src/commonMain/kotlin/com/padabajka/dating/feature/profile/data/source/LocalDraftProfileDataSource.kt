package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import kotlinx.coroutines.flow.Flow

interface LocalDraftProfileDataSource {
    val profile: Flow<DraftProfile>
    suspend fun replace(profile: DraftProfile)

    suspend fun update(action: (DraftProfile) -> DraftProfile)
}
