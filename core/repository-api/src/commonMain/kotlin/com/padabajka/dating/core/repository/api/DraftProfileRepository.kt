package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.exception.ProfileException
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface DraftProfileRepository {
    val profile: Flow<Profile>

    @Throws(ProfileException::class, CancellationException::class)
    suspend fun update(action: (Profile) -> Profile)

    @Throws(ProfileException::class, CancellationException::class)
    suspend fun saveUpdates()
    suspend fun discardUpdates()
}
