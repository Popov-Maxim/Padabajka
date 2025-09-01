package com.padabajka.dating.feature.profile.domain.update

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.Gender
import kotlin.coroutines.cancellation.CancellationException

class UpdateUserGenderUseCase(
    private val repository: DraftProfileRepository
) {
    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(gender: Gender) {
        repository.update { profile ->
            profile.copy(
                gender = gender
            )
        }
    }
}
