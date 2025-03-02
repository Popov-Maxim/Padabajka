package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class SaveProfileUseCase(
    private val profileRepository: ProfileRepository
) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(updateProfile: (Profile) -> Profile) {
        val currentProfile = with(profileRepository) { profileValue ?: profile.first() }
        val newProfile = updateProfile(currentProfile)
        profileRepository.replace(newProfile)
    }
}
