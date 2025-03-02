package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.DraftProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import kotlinx.datetime.LocalDate
import kotlin.coroutines.cancellation.CancellationException

@Deprecated("need delete")
class BirthdayUpdateUseCase(private val repository: DraftProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(birthday: LocalDate) {
        repository.update { profile ->
            profile.copy(birthday = birthday)
        }
    }
}
