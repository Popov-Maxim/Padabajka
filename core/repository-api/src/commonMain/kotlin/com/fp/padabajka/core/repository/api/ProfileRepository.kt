package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    suspend fun update(action: (Profile) -> Profile)
}