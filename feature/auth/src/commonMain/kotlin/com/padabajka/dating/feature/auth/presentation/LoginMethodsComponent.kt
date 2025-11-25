package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.feature.auth.domain.method.GoogleLoginUseCase
import com.padabajka.dating.feature.auth.presentation.model.LoginMethodEvent

class LoginMethodsComponent(
    context: ComponentContext,
    private val goToEmailMethodScreen: () -> Unit,
    private val goToDebugMethodScreen: () -> Unit,
    private val googleLoginUseCase: GoogleLoginUseCase,
) : BaseComponent<EmptyState>(context, EmptyState) {
    fun onEvent(event: LoginMethodEvent) {
        when (event) {
            LoginMethodEvent.SelectEmailMethod -> goToEmailMethodScreen()
            LoginMethodEvent.SelectGoogleMethod -> loginGoogle()
            LoginMethodEvent.SelectDebugMethod -> goToDebugMethodScreen()
        }
    }

    private fun loginGoogle() = mapAndReduceException(
        action = {
            googleLoginUseCase.login()
        },
        mapper = {
            it
        },
        update = { state, _ -> state }
    )
}
