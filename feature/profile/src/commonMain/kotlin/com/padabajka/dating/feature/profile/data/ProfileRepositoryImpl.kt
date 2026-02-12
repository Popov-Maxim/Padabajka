package com.padabajka.dating.feature.profile.data

import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.exception.ResourceExceptions
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.profile.ProfileState
import com.padabajka.dating.core.repository.api.model.profile.rawProfile
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.profile.data.source.RemoveProfileDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update

class ProfileRepositoryImpl(
    private val removeProfileDataSource: RemoveProfileDataSource
) : ProfileRepository {
    override val profile: Flow<Profile>
        get() = profileState.mapNotNull { it.rawProfile() }
    override val profileValue: Profile?
        get() = _profileState.value.rawProfile()
    private val _profileState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Idle)
    override val profileState: Flow<ProfileState> = _profileState

    override suspend fun updateProfile() {
        val profile = removeProfileDataSource.getProfile()
        _profileState.setProfile(profile)
    }

    override suspend fun replace(profile: Profile) {
        removeProfileDataSource.replace(profileValue, profile)
        _profileState.setProfile(profile)
    }

    override suspend fun create(profile: Profile, gender: Gender) {
        removeProfileDataSource.create(profile, gender)
        _profileState.setProfile(profile)
    }

    override suspend fun profile(userId: PersonId): Profile? {
        return try {
            removeProfileDataSource.getProfile(userId.raw)
        } catch (_: ResourceExceptions.Deleted) {
            null
        }
    }

    override suspend fun setFreeze(freeze: Boolean) {
        removeProfileDataSource.setFreeze(freeze)
        _profileState.updateProfile { profile: Profile ->
            profile.copy(
                isFrozen = freeze
            )
        }
    }

    private fun MutableStateFlow<ProfileState>.setProfile(profile: Profile?) {
        value = if (profile == null) {
            ProfileState.NotCreated
        } else {
            ProfileState.Existing(profile)
        }
    }

    private fun MutableStateFlow<ProfileState>.updateProfile(updated: (Profile) -> Profile) {
        update {
            when (it) {
                is ProfileState.Existing -> it.copy(profile = updated(it.profile))
                ProfileState.Idle -> it
                ProfileState.NotCreated -> it
            }
        }
    }
}
