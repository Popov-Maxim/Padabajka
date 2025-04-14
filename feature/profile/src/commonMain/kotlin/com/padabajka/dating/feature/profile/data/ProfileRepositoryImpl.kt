package com.padabajka.dating.feature.profile.data

import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.profile.data.source.RemoveProfileDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class ProfileRepositoryImpl(
    private val removeProfileDataSource: RemoveProfileDataSource
) : ProfileRepository {
    private val _profile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    override val profile: Flow<Profile> = _profile.filterNotNull()
    override val profileValue: Profile?
        get() = _profile.value

    override suspend fun updateProfile() {
        val profile = removeProfileDataSource.getProfile()
        _profile.value = profile
    }

    override suspend fun replace(profile: Profile) {
        removeProfileDataSource.replace(_profile.value, profile)
        _profile.value = profile
    }

    override suspend fun profile(userId: PersonId): Profile {
        return removeProfileDataSource.getProfile(userId.raw)
    }
}
