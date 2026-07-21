package com.padabajka.dating.feature.legal.presentation.model

sealed interface NewLegalAgreementsEvent {
    data object Apply : NewLegalAgreementsEvent
    data object Logout : NewLegalAgreementsEvent
}
