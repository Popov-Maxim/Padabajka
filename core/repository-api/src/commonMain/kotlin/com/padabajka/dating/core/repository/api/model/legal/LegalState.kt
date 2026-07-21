package com.padabajka.dating.core.repository.api.model.legal

sealed interface LegalState {
    data object Idle : LegalState
    data object AllAccepted : LegalState
    data class NeedAccent(
        val privacy: String,
        val terms: String,
    ) : LegalState
}
