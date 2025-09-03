package com.padabajka.dating.feature.profile.domain.update

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.Image

class UpdateMainImageUseCase(
    private val repository: DraftProfileRepository
) {

    @Throws(ProfileException::class)
    suspend operator fun invoke(image: Image) {
        repository.update {
            it.copy(mainImage = image)
        }
    }
}
