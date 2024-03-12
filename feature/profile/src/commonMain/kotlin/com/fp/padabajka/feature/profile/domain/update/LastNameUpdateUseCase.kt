package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository

class LastNameUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke(lastName: String) {
        repository.update { profile ->
            profile.copy(
                lastName = lastName
            )
        }
    }
}
