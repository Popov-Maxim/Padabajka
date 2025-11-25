package com.padabajka.dating.feature.auth.presentation.model

sealed interface LoginMethodEvent {
    data object SelectEmailMethod : LoginMethodEvent
    data object SelectGoogleMethod : LoginMethodEvent
    data object SelectDebugMethod : LoginMethodEvent
}

sealed interface EmailLoginMethodEvent {
    data class EmailChanged(val email: String) : EmailLoginMethodEvent
    data object ChangeEmail : EmailLoginMethodEvent

//    data object EmailFieldLoosFocus : EmailLoginMethodEvent
    data object LoginClick : EmailLoginMethodEvent
    data object BackToLoginMethods : EmailLoginMethodEvent

    data object OpenEmailApp : EmailLoginMethodEvent
}

sealed interface DebugLoginMethodEvent {
    data class UUIDChanged(val uuid: String) : DebugLoginMethodEvent
    data object LoginClick : DebugLoginMethodEvent
    data object BackToLoginMethods : DebugLoginMethodEvent
}
