package com.padabajka.dating.settings.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.domain.sync.SyncRemoteDataUseCase
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.push.data.domain.SaveTokenUseCase
import com.padabajka.dating.settings.domain.AppSettingsComponentProvider
import com.padabajka.dating.settings.domain.DeleteAccountUseCase
import com.padabajka.dating.settings.presentation.model.DeleteAccountEvent
import com.padabajka.dating.settings.presentation.model.FreezeAccountEvent
import com.padabajka.dating.settings.presentation.model.LogOutEvent
import com.padabajka.dating.settings.presentation.model.NavigateBackEvent
import com.padabajka.dating.settings.presentation.model.OpenLanguageSelectorEvent
import com.padabajka.dating.settings.presentation.model.RequestPermissionEvent
import com.padabajka.dating.settings.presentation.model.SendPushToken
import com.padabajka.dating.settings.presentation.model.SettingsEvent
import com.padabajka.dating.settings.presentation.model.SettingsState
import com.padabajka.dating.settings.presentation.model.SyncData
import com.padabajka.dating.settings.presentation.model.UnfreezeAccountEvent
import com.padabajka.dating.settings.presentation.setting.SettingNavigator
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val settingNavigator: SettingNavigator,
    logoutUseCaseFactory: Factory<LogOutUseCase>,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val syncRemoteDataUseCase: SyncRemoteDataUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val profileRepository: ProfileRepository,
    settingsComponentProvider: AppSettingsComponentProvider,
    private val subscriptionRepository: SubscriptionRepository,
    private val alertService: AlertService
) : BaseComponent<SettingsState>(
    context,
    "setting",
    SettingsState(
        selectedLanguage = Language.Static.EN,
        subscriptionActive = subscriptionRepository.subscriptionStateValue.isActive,
        profileFrozen = profileRepository.profileValue?.isFrozen ?: false
    )
) {

    private val logoutUseCase by logoutUseCaseFactory.delegate()

    init {
        init(settingsComponentProvider)

        componentScope.launch {
            subscriptionRepository.subscriptionState.map {
                it.isActive
            }.distinctUntilChanged().collect { newState ->
                reduce {
                    it.copy(
                        subscriptionActive = newState
                    )
                }
            }
        }
        componentScope.launch {
            profileRepository.profile.map {
                it.isFrozen
            }.distinctUntilChanged().collect { newState ->
                reduce {
                    it.copy(
                        profileFrozen = newState
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            LogOutEvent -> logout()
            NavigateBackEvent -> navigateBack()
            SendPushToken -> sendPushToken()
            SyncData -> syncRemoteData()
            RequestPermissionEvent -> settingNavigator.openPermissionFlow()
            OpenLanguageSelectorEvent -> settingNavigator.openLanguageSelector()
            DeleteAccountEvent -> deleteAccount()
            FreezeAccountEvent -> changeFreeze(true)
            UnfreezeAccountEvent -> changeFreeze(false)
            SettingsEvent.OpenSubscription -> settingNavigator.openSubscriptionScreen()
        }
    }

    private fun logout() = launchStep(
        action = {
            logoutUseCase()
        },
        onError = ::defaultOnError
    )

    private fun sendPushToken() = launchStep(
        // TODO(P3): dont run in prod
        action = {
            saveTokenUseCase()
        },
    )

    private fun syncRemoteData() = launchStep(
        // TODO(P3): dont run in prod
        action = {
            syncRemoteDataUseCase()
        },
    )

    private fun deleteAccount() = launchStep(
        action = {
            deleteAccountUseCase()
        },
        onError = ::defaultOnError
    )

    private fun changeFreeze(freeze: Boolean) = launchStep(
        action = {
            profileRepository.setFreeze(freeze)
        },
        onError = ::defaultOnError
    )

    private suspend fun defaultOnError(error: ExternalDomainError): Boolean {
        val error = when (error) {
            is ExternalDomainError.TextError -> error
            is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
        }

        alertService.showAlert { error.text.translate() }
        return error.needLog.not()
    }

    private fun init(settingsComponentProvider: AppSettingsComponentProvider) =
        launchStep(
            action = {
                settingsComponentProvider.languages.collect { lang ->
                    reduce {
                        it.copy(selectedLanguage = lang)
                    }
                }
            },
        )
}
