package com.fp.padabajka.feature.profile.data

import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// TODO(profile): add impl
class ProfileRepositoryImpl : ProfileRepository {
    override val profile: Flow<Profile>
        get() = flow {}

    override suspend fun replace(profile: Profile) = Unit
}
