package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository
import kotlinx.datetime.LocalDate

class BirthdayUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke(birthday: LocalDate) {
        repository.update { profile ->
            profile.copy(birthday = birthday)
        }
    }
}
