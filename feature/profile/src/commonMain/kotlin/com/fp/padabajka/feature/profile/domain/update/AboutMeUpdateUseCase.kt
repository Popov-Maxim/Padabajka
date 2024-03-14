package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import kotlin.coroutines.cancellation.CancellationException

class AboutMeUpdateUseCase(private val repository: EditProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(aboutMe: String) {
        repository.update { profile ->
            profile.copy(aboutMe = aboutMe)
        }
    }
}
