package com.fp.padabajka.navigation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.repository.api.model.auth.LoggedIn
import com.fp.padabajka.core.repository.api.model.auth.LoggedOut
import com.fp.padabajka.core.repository.api.model.auth.WaitingForEmailValidation
import com.fp.padabajka.feature.auth.domain.AuthStateProvider
import com.fp.padabajka.feature.auth.presentation.VerificationComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class AuthStateObserverComponent(
    context: ComponentContext
) : NavigateComponentContext<AuthStateObserverComponent.Configuration, AuthStateObserverComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.SplashScreen
),
    KoinComponent {

    private val authProvider: AuthStateProvider = get()

    suspend fun subscribeToAuth() {
        authProvider.authState.collect { authState ->
            when (authState) {
                is LoggedIn -> navigateNewStack(Configuration.AuthScope)
                LoggedOut -> navigateNewStack(Configuration.UnauthScope)
                is WaitingForEmailValidation -> navigateNewStack(Configuration.VerificationScreen)
            }
        }
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SplashScreen -> Child.SplashScreen
            Configuration.UnauthScope -> Child.UnauthScope(
                component = UnauthScopeNavigateComponent(context)
            )
            Configuration.AuthScope -> Child.AuthScope(
                component = AuthScopeNavigateComponent(context)
            )
            Configuration.VerificationScreen -> Child.VerificationScreen(
                component = get {
                    parametersOf(context)
                }
            )
        }
    }

    sealed interface Child {
        data object SplashScreen : Child
        data class UnauthScope(val component: UnauthScopeNavigateComponent) : Child
        data class VerificationScreen(val component: VerificationComponent) : Child
        data class AuthScope(val component: AuthScopeNavigateComponent) : Child
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SplashScreen : Configuration

        @Serializable
        data object UnauthScope : Configuration

        @Serializable
        data object VerificationScreen : Configuration

        @Serializable
        data object AuthScope : Configuration
    }
}
