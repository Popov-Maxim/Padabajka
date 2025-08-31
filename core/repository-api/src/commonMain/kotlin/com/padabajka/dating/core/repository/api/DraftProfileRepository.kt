package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface DraftProfileRepository {
    val profile: Flow<DraftProfile>
    val profileValue: DraftProfile?

    @Throws(ProfileException::class, CancellationException::class)
    suspend fun update(action: (DraftProfile) -> DraftProfile)
}
