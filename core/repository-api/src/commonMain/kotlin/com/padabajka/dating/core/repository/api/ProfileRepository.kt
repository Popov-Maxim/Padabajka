package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.profile.ProfileState
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    val profileValue: Profile?
    val profileState: Flow<ProfileState> // TODO: use only profileState, now use for start navigation
    suspend fun updateProfile()
    suspend fun replace(profile: Profile)
    suspend fun create(profile: Profile, gender: Gender)
    suspend fun profile(userId: PersonId): Profile?
}
