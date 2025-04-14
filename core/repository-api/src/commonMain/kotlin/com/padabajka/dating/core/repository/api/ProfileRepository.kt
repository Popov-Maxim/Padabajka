package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    val profileValue: Profile?
    suspend fun updateProfile()
    suspend fun replace(profile: Profile)
    suspend fun profile(userId: PersonId): Profile
}
