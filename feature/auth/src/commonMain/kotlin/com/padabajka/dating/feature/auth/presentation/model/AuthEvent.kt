package com.padabajka.dating.feature.auth.presentation.model

sealed interface LoginMethodEvent {
    data object SelectEmailMethod : LoginMethodEvent
    data object SelectGoogleMethod : LoginMethodEvent
}

sealed interface EmailLoginMethodEvent {
    data class EmailChanged(val email: String) : EmailLoginMethodEvent

//    data object EmailFieldLoosFocus : EmailLoginMethodEvent
    data object LoginClick : EmailLoginMethodEvent
    data object BackToLoginMethods : EmailLoginMethodEvent
}
