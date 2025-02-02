package com.fp.padabajka.feature.profile.data.source

import com.fp.padabajka.core.repository.api.model.profile.Profile

interface RemoveProfileDataSource {
    suspend fun getProfile(): Profile
    suspend fun replace(current: Profile?, newProfile: Profile)
}
