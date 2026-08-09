package com.padabajka.dating.core.repository.api.metadata

import com.padabajka.dating.core.repository.api.model.legal.LegalState
import com.padabajka.dating.core.repository.api.model.legal.LegalVersions
import kotlinx.coroutines.flow.Flow

interface LegalRepository {
    val userLegalState: Flow<LegalState>
    val acceptedLegalVersions: Flow<LegalVersions>
    val acceptedLegalVersionsValue: LegalVersions?
    suspend fun actualVersions(): LegalVersions
    suspend fun updateUserLegalState()
    suspend fun acceptLegal(versions: LegalVersions)
    suspend fun clearLocalData()
}
