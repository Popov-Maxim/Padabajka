package com.padabajka.dating.feature.auth.presentation.model

sealed interface VerificationEvent

data object ResendVerification : VerificationEvent
data object ContinueVerification : VerificationEvent
