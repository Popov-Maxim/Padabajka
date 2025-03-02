package com.fp.padabajka.feature.profile.data.source

import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.ProfileIsNullException
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface LocalDraftProfileDataSource {
    val profile: Flow<Profile?>
    suspend fun replace(profile: Profile?)

    @Throws(ProfileIsNullException::class, CancellationException::class)
    suspend fun update(action: (Profile) -> Profile)
}
