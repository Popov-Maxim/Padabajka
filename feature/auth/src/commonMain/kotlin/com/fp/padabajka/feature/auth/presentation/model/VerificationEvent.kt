package com.fp.padabajka.feature.auth.presentation.model

sealed interface VerificationEvent

data object ResendVerification : VerificationEvent
data object ContinueVerification : VerificationEvent
