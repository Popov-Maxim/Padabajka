package com.padabajka.dating.feature.profile.domain

import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.profile.domain.creator.DraftProfileProvider
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException

class CreateProfileFromDraftUseCase(
    private val draftProfileProvider: DraftProfileProvider,
    private val loadImageUseCase: LoadImageUseCase,
    private val profileRepository: ProfileRepository
) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke() {
        val currentDraftProfile = with(draftProfileProvider) { getProfile() ?: profile.first() }
        val profile = currentDraftProfile.toProfile()
        val profileWithLoadingImage = profile.loadedImage()

        profileRepository.create(profileWithLoadingImage, currentDraftProfile.gender!!)
    }

    private suspend fun Profile.loadedImage(): Profile {
        val newImages = loadImageUseCase(images).toPersistentList()

        return copy(images = newImages)
    }

    private fun DraftProfile.toProfile(): Profile { // TODO: add check if null
        return Profile(
            name = name!!,
            birthday = birthday!!,
            images = listOfNotNull(mainImage) + images,
            aboutMe = aboutMe ?: "",
            lookingFor = lookingFor!!,
            details = details,
            lifestyles = lifestyles,
            mainAchievement = null,
            achievements = emptyList(),
        )
    }
}
