package com.padabajka.dating.feature.permission.flow.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.permission.GeoPermissionController
import com.padabajka.dating.core.permission.NotificationPermissionController
import com.padabajka.dating.core.presentation.NavigateComponentContext
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class PermissionFlowComponent(
    context: ComponentContext,
    private val finish: () -> Unit,
    private val geoPermissionController: GeoPermissionController,
    private val notificationPermissionController: NotificationPermissionController,
) : NavigateComponentContext<PermissionFlowComponent.Configuration, PermissionFlowComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.SplashScreen
) {

    init {
        nextScreen(PermissionScreen.None)
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SplashScreen -> Child.SplashScreen
            Configuration.FinishScreen -> Child.FinishScreen {
                finish()
            }
            Configuration.GeoPermissionScreen -> Child.GeoPermissionScreen(
                PermissionComponent(
                    context,
                    geoPermissionController,
                    { nextScreen(PermissionScreen.Geo) },
                    { nextScreen(PermissionScreen.Geo) }
                )
            )

            Configuration.NotificationPermissionScreen -> Child.NotificationPermissionScreen(
                PermissionComponent(
                    context,
                    notificationPermissionController,
                    { nextScreen(PermissionScreen.Notification) },
                    { nextScreen(PermissionScreen.Notification) }
                )
            )
        }
    }

    private fun nextScreen(currentScreen: PermissionScreen) {
        navigateScope.launch {
            when {
                currentScreen < PermissionScreen.Geo && geoPermissionController.hasPermission()
                    .not() ->
                    navigate(Configuration.GeoPermissionScreen)

                currentScreen < PermissionScreen.Notification &&
                    notificationPermissionController.hasPermission().not() ->
                    navigate(Configuration.NotificationPermissionScreen)

                currentScreen == PermissionScreen.None ->
                    navigate(Configuration.FinishScreen)

                else -> finish()
            }
        }
    }

    sealed interface Child {
        data object SplashScreen : Child
        data class FinishScreen(val onApply: () -> Unit) : Child
        data class GeoPermissionScreen(val permissionComponent: PermissionComponent) : Child
        data class NotificationPermissionScreen(val permissionComponent: PermissionComponent) :
            Child
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SplashScreen : Configuration

        @Serializable
        data object FinishScreen : Configuration

        @Serializable
        data object GeoPermissionScreen : Configuration

        @Serializable
        data object NotificationPermissionScreen : Configuration
    }

    private enum class PermissionScreen {
        None,
        Geo,
        Notification
    }
}
