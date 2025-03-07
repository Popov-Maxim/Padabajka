package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    val profileValue: Profile?
    suspend fun updateProfile()
    suspend fun replace(profile: Profile)
}
