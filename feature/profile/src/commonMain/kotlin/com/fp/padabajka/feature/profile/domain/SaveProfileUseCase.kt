package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.ImageRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class SaveProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val imageRepository: ImageRepository,
) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(updateProfile: (Profile) -> Profile) {
        val currentProfile = with(profileRepository) { profileValue ?: profile.first() }
        val newProfile = updateProfile(currentProfile)
        val profileWithLoadingImage = newProfile.loadedImage()

        profileRepository.replace(profileWithLoadingImage)
    }

    private suspend fun Profile.loadedImage(): Profile {
        val newImages = this.images.map {
            when (it) {
                is Image.ByteArray -> imageRepository.uploadImage(it)
                is Image.Local -> {
                    val image = imageRepository.getLocalImage(it)
                    imageRepository.uploadImage(image)
                }
                is Image.Url -> it
            }
        }.toPersistentList()

        return copy(images = newImages)
    }
}
