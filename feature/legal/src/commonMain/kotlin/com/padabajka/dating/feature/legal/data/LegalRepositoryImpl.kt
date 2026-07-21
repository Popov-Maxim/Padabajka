package com.padabajka.dating.feature.legal.data

import com.padabajka.dating.core.repository.api.metadata.LegalRepository
import com.padabajka.dating.core.repository.api.model.legal.LegalState
import com.padabajka.dating.core.repository.api.model.legal.LegalVersions
import com.padabajka.dating.feature.legal.data.network.LegalApi
import com.padabajka.dating.feature.legal.data.network.model.toDomain
import com.padabajka.dating.feature.legal.data.network.model.toRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class LegalRepositoryImpl(
    private val legalApi: LegalApi
) : LegalRepository {
    private var actualVersions: LegalVersions? = null

    private suspend inline fun actualVersionsOrInit(init: suspend () -> LegalVersions): LegalVersions {
        return actualVersions ?: init().also { actualVersions = it }
    }

    private val _userLegalState: MutableStateFlow<LegalState> = MutableStateFlow(LegalState.Idle)
    override val userLegalState: Flow<LegalState>
        get() = _userLegalState.asStateFlow()
    private val _acceptedLegalVersions: MutableStateFlow<LegalVersions?> = MutableStateFlow(null)
    override val acceptedLegalVersions: Flow<LegalVersions>
        get() = _acceptedLegalVersions.asStateFlow().filterNotNull()
    override val acceptedLegalVersionsValue: LegalVersions?
        get() = _acceptedLegalVersions.value

    override suspend fun actualVersions(): LegalVersions {
        return actualVersionsOrInit {
            val actual = legalApi.getActual()

            actual.toDomain().also { actualVersions = it }
        }
    }

    override suspend fun updateUserLegalState() {
        val response = legalApi.getAccept()
        val userLegalVersions = response.toDomain()
        _acceptedLegalVersions.value = userLegalVersions.accepted
        _userLegalState.value = if (response.needUpdate) {
            LegalState.NeedAccent(
                privacy = userLegalVersions.actual.privacy,
                terms = userLegalVersions.actual.terms
            )
        } else {
            LegalState.AllAccepted
        }
    }

    override suspend fun acceptLegal(versions: LegalVersions) {
        legalApi.postAccepted(versions.toRequest())
        _acceptedLegalVersions.value = versions
        _userLegalState.value = LegalState.AllAccepted
    }
}
