package com.padabajka.dating.settings.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.feature.permission.flow.presentation.PermissionFlowComponent
import com.padabajka.dating.feature.subscription.presentation.SubscriptionScreenComponent
import com.padabajka.dating.settings.presentation.setting.LanguageSelectorComponent
import com.padabajka.dating.settings.presentation.setting.SettingNavigator
import kotlinx.serialization.Serializable
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class SettingsScopeNavigateComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
) : NavigateComponentContext<SettingsScopeNavigateComponent.Configuration, SettingsScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.MainSettingScreen
) {

    private val settingNavigator: SettingNavigator = object : SettingNavigator {
        override fun openLanguageSelector() {
            navigate(Configuration.LanguageSelectorScreen)
        }

        override fun openPermissionFlow() {
            navigate(Configuration.PermissionFlowScreen)
        }

        override fun openSubscriptionScreen() {
            navigate(Configuration.SubscriptionScreen)
        }
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.LanguageSelectorScreen -> Child.LanguageSelectorScreen(
                component = LanguageSelectorComponent(
                    context = context,
                    navigateBack = { navigateBack() },
                    changeLanguageUseCase = get(),
                    appSettingsComponentProvider = get()
                )
            )
            Configuration.MainSettingScreen -> Child.MainSettingScreen(
                component = get {
                    parametersOf(
                        context,
                        navigateBack,
                        settingNavigator
                    )
                }
            )
            Configuration.PermissionFlowScreen -> Child.PermissionFlowScreen(
                component = PermissionFlowComponent(
                    context = context,
                    finish = ::navigateBack,
                    geoPermissionController = get(),
                    notificationPermissionController = get(),
                )
            )

            Configuration.SubscriptionScreen -> Child.SubscriptionScreen(
                component = SubscriptionScreenComponent(
                    context = context,
                    navigateBack = ::navigateBack,
                    subscriptionRepository = get()
                )
            )
        }
    }

    sealed interface Child {
        data class MainSettingScreen(val component: SettingScreenComponent) : Child
        data class LanguageSelectorScreen(val component: LanguageSelectorComponent) : Child
        data class SubscriptionScreen(val component: SubscriptionScreenComponent) : Child

        data class PermissionFlowScreen(val component: PermissionFlowComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object MainSettingScreen : Configuration

        @Serializable
        data object LanguageSelectorScreen : Configuration

        @Serializable
        data object PermissionFlowScreen : Configuration

        @Serializable
        data object SubscriptionScreen : Configuration
    }
}
