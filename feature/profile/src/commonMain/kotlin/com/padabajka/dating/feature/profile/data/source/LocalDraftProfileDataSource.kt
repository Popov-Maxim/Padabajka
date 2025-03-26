package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.profile.data.ProfileIsNullException
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface LocalDraftProfileDataSource {
    val profile: Flow<Profile?>
    suspend fun replace(profile: Profile?)

    @Throws(ProfileIsNullException::class, CancellationException::class)
    suspend fun update(action: (Profile) -> Profile)
}
