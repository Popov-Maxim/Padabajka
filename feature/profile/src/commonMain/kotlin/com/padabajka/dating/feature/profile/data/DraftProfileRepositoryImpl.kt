package com.padabajka.dating.feature.profile.data

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import com.padabajka.dating.feature.profile.data.source.LocalDraftProfileDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class DraftProfileRepositoryImpl(
    coroutineScope: CoroutineScope,
    private val localDraftProfileDataSource: LocalDraftProfileDataSource,
) : DraftProfileRepository {

    private val _profileState = localDraftProfileDataSource.profile
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    override val profile: Flow<DraftProfile>
        get() = _profileState.filterNotNull()
    override val profileValue: DraftProfile?
        get() = _profileState.value

    override suspend fun update(action: (DraftProfile) -> DraftProfile) {
        localDraftProfileDataSource.update(action)
    }
}
