package com.padabajka.dating.feature.profile.domain.update

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.LookingForData

class UpdateLookingForUseCase(
    private val repository: DraftProfileRepository
) {
    @Throws(ProfileException::class)
    suspend operator fun invoke(lookingFor: LookingForData) {
        repository.update {
            it.copy(lookingFor = lookingFor)
        }
    }
}
