package com.fp.padabajka.feature.profile.data

import com.fp.padabajka.core.repository.api.DraftProfileRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.exception.ProfileException
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.source.LocalDraftProfileDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.cancellation.CancellationException

class DraftProfileRepositoryImpl(
    private val profileRepository: ProfileRepository,
    private val localDraftProfileDataSource: LocalDraftProfileDataSource,
) : DraftProfileRepository {

    override val profile: Flow<Profile>
        get() = localDraftProfileDataSource.profile.flatMapLatest {
            if (it == null) {
                profileRepository.profile.onEach { profile ->
                    localDraftProfileDataSource.replace(profile)
                }
            } else {
                flowOf(it)
            }
        }.distinctUntilChanged()

    @Throws(ProfileException::class, CancellationException::class)
    override suspend fun update(action: (Profile) -> Profile) {
        localDraftProfileDataSource.update(action) // TODO: Optimization
    }

    @Throws(ProfileException::class, CancellationException::class)
    override suspend fun saveUpdates() {
        val profile = localDraftProfileDataSource.profile.first() ?: throw ProfileIsNullException
        profileRepository.replace(profile)
    }

    override suspend fun discardUpdates() {
        localDraftProfileDataSource.replace(null)
    }
}
