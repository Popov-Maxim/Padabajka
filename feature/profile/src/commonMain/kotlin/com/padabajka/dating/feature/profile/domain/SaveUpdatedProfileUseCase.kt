package com.padabajka.dating.feature.profile.domain

import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class SaveUpdatedProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val loadImageUseCase: LoadImageUseCase,
) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(updateProfile: (Profile) -> Profile) {
        val currentProfile = with(profileRepository) { profileValue ?: profile.first() }
        val newProfile = updateProfile(currentProfile)
        val profileWithLoadingImage = newProfile.loadedImage()

        profileRepository.replace(profileWithLoadingImage)
    }

    private suspend fun Profile.loadedImage(): Profile {
        val newImages = loadImageUseCase(images).toPersistentList()

        return copy(images = newImages)
    }
}
