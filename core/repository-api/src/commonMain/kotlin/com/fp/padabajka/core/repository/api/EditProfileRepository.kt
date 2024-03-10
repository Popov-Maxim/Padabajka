package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.exception.ProfileException
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface EditProfileRepository {
    val profile: Flow<Profile>
    suspend fun update(action: (Profile) -> Profile)

    @Throws(ProfileException::class, CancellationException::class)
    suspend fun saveUpdate()
    suspend fun discardUpdate()
}
