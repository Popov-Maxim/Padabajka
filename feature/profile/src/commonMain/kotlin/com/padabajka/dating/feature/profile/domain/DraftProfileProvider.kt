package com.padabajka.dating.feature.profile.domain

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

class DraftProfileProvider(
    private val draftProfileRepository: DraftProfileRepository
) {
    val profile: Flow<Profile>
        get() = draftProfileRepository.profile
}
