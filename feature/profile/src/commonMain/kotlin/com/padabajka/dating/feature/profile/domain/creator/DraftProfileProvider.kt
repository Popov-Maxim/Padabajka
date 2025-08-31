package com.padabajka.dating.feature.profile.domain.creator

import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import kotlinx.coroutines.flow.Flow

class DraftProfileProvider(private val repository: DraftProfileRepository) {
    val profile: Flow<DraftProfile>
        get() = repository.profile

    fun getProfile(): DraftProfile? = repository.profileValue
}
