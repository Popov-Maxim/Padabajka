package com.fp.padabajka.feature.profile.data.source

import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface LocalEditProfileDataSource {
    val profile: Flow<Profile?>
    suspend fun replace(profile: Profile?)
    suspend fun update(action: (Profile) -> Profile)
}
