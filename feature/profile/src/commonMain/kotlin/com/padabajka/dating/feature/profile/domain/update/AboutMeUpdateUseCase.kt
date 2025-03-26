package com.padabajka.dating.feature.profile.domain.update

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.exception.ProfileException
import kotlin.coroutines.cancellation.CancellationException

@Deprecated("need delete")
class AboutMeUpdateUseCase(private val repository: DraftProfileRepository) {

    @Throws(ProfileException::class, CancellationException::class)
    suspend operator fun invoke(aboutMe: String) {
        repository.update { profile ->
            profile.copy(aboutMe = aboutMe)
        }
    }
}
