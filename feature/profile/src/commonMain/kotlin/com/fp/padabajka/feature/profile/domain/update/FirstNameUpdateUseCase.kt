package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.DraftProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import kotlin.coroutines.cancellation.CancellationException

@Deprecated("need delete")
class FirstNameUpdateUseCase(private val repository: DraftProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(firstName: String) {
        repository.update { profile ->
            profile.copy(
                firstName = firstName
            )
        }
    }
}
