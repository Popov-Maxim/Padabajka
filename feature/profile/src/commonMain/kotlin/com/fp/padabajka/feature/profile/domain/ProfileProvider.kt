package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

class ProfileProvider(
    private val editProfileRepository: EditProfileRepository
) {
    val profile: Flow<Profile>
        get() = editProfileRepository.profile
}
