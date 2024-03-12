package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository

class FirstNameUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke(firstName: String) {
        repository.update { profile ->
            profile.copy(
                firstName = firstName
            )
        }
    }
}
