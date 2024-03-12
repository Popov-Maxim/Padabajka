package com.fp.padabajka.feature.profile.data

import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.source.LocalEditProfileDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.cancellation.CancellationException

class EditProfileRepositoryImpl(
    private val profileRepository: ProfileRepository,
    private val localEditProfileDataSource: LocalEditProfileDataSource,
) : EditProfileRepository {

    override val profile: Flow<Profile>
        get() = localEditProfileDataSource.profile.flatMapLatest {
            if (it == null) {
                profileRepository.profile.onEach { profile ->
                    localEditProfileDataSource.replace(profile)
                }
            } else {
                flowOf(it)
            }
        }.distinctUntilChanged()

    override suspend fun update(action: (Profile) -> Profile) {
        localEditProfileDataSource.update(action) // TODO: Optimization
    }

    @Throws(ProfileException::class, CancellationException::class)
    override suspend fun saveUpdate() {
        val profile = localEditProfileDataSource.profile.first() ?: throw ProfileIsNullException
        profileRepository.replace(profile)
    }

    override suspend fun discardUpdate() {
        localEditProfileDataSource.replace(null)
    }
}
