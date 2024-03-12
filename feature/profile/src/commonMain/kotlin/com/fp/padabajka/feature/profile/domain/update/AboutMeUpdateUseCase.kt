package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository

class AboutMeUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke(aboutMe: String) {
        repository.update { profile ->
            profile.copy(aboutMe = aboutMe)
        }
    }
}
