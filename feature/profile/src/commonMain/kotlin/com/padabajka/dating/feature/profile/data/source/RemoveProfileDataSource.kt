package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Profile

interface RemoveProfileDataSource {
    suspend fun getProfile(): Profile?
    suspend fun getProfile(userId: String): Profile
    suspend fun replace(current: Profile?, newProfile: Profile)
    suspend fun create(profile: Profile, gender: Gender)
}
