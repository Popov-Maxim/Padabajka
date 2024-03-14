package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import kotlin.coroutines.cancellation.CancellationException

class SaveUpdateProfileUseCase(private val repository: EditProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke() {
        repository.saveUpdates()
    }
}
