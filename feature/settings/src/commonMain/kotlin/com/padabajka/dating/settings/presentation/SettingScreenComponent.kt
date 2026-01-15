package com.padabajka.dating.settings.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.domain.sync.SyncRemoteDataUseCase
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.push.data.domain.SaveTokenUseCase
import com.padabajka.dating.settings.domain.AppSettingsComponentProvider
import com.padabajka.dating.settings.presentation.model.LogOutEvent
import com.padabajka.dating.settings.presentation.model.NavigateBackEvent
import com.padabajka.dating.settings.presentation.model.OpenLanguageSelectorEvent
import com.padabajka.dating.settings.presentation.model.RequestPermissionEvent
import com.padabajka.dating.settings.presentation.model.SendPushToken
import com.padabajka.dating.settings.presentation.model.SettingsEvent
import com.padabajka.dating.settings.presentation.model.SettingsState
import com.padabajka.dating.settings.presentation.model.SyncData
import com.padabajka.dating.settings.presentation.setting.SettingNavigator

class SettingScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val settingNavigator: SettingNavigator,
    logoutUseCaseFactory: Factory<LogOutUseCase>,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val syncRemoteDataUseCase: SyncRemoteDataUseCase,
    settingsComponentProvider: AppSettingsComponentProvider
) : BaseComponent<SettingsState>(
    context,
    SettingsState(
        selectedLanguage = Language.Static.EN
    )
) {

    private val logoutUseCase by logoutUseCaseFactory.delegate()

    init {
        init(settingsComponentProvider)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            LogOutEvent -> logout()
            NavigateBackEvent -> navigateBack()
            SendPushToken -> sendPushToken()
            SyncData -> syncRemoteData()
            RequestPermissionEvent -> settingNavigator.openPermissionFlow()
            OpenLanguageSelectorEvent -> settingNavigator.openLanguageSelector()
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

    private fun syncRemoteData() = mapAndReduceException(
        action = {
            syncRemoteDataUseCase()
        },
        mapper = { it },
        update = { state, m ->
            state
        }
    )

    private fun init(settingsComponentProvider: AppSettingsComponentProvider) =
        mapAndReduceException(
            action = {
                settingsComponentProvider.languages.collect { lang ->
                    reduce {
                        it.copy(selectedLanguage = lang)
                    }
                }
            },
            mapper = { it },
            update = { state, m ->
                state
            }
        )
}
