package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.DraftProfileRepository
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

class DraftProfileProvider(
    private val draftProfileRepository: DraftProfileRepository
) {
    val profile: Flow<Profile>
        get() = draftProfileRepository.profile
}
