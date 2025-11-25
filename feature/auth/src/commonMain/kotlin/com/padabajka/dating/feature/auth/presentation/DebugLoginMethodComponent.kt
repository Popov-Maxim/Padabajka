package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.feature.auth.presentation.model.DebugLoggingInState
import com.padabajka.dating.feature.auth.presentation.model.DebugLoginMethodEvent

class DebugLoginMethodComponent(
    context: ComponentContext,
    private val goToLoginMethodScreen: () -> Unit,
    private val authRepository: AuthRepository
) : BaseComponent<DebugLoggingInState>(context, DebugLoggingInState()) {

    fun onEvent(event: DebugLoginMethodEvent) {
        when (event) {
            DebugLoginMethodEvent.BackToLoginMethods -> goToLoginMethodScreen()
            is DebugLoginMethodEvent.UUIDChanged -> updateUUID(event.uuid)
            DebugLoginMethodEvent.LoginClick -> loginDebug()
        }
    }

    private fun updateUUID(uuid: String) = reduce { state ->
        state.copy(uuid = uuid)
    }

    private fun loginDebug() = mapAndReduceException(
        action = {
            authRepository.loginDebug(state.value.uuid)
        },
        mapper = {
            it
        },
        update = { state, _ -> state }
    )
}
