package com.padabajka.dating.settings.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.push.data.domain.SaveTokenUseCase
import com.padabajka.dating.settings.presentation.model.LogOutEvent
import com.padabajka.dating.settings.presentation.model.NavigateBackEvent
import com.padabajka.dating.settings.presentation.model.SendPushToken
import com.padabajka.dating.settings.presentation.model.SettingsEvent

class SettingScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    logoutUseCaseFactory: Factory<LogOutUseCase>,
    private val saveTokenUseCase: SaveTokenUseCase,
) : BaseComponent<EmptyState>(
    context,
    EmptyState
) {

    private val logoutUseCase by logoutUseCaseFactory.delegate()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            LogOutEvent -> logout()
            NavigateBackEvent -> navigateBack()
            SendPushToken -> sendPushToken()
        }
    }

    private fun logout() = mapAndReduceException(
        action = {
            logoutUseCase()
        },
        mapper = { it },
        update = { state, m ->
            state
        }
    )

    private fun sendPushToken() = mapAndReduceException(
        action = {
            saveTokenUseCase()
        },
        mapper = { it },
        update = { state, m ->
            state
        }
    )
}
