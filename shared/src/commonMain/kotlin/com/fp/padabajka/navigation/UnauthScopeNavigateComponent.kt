package com.fp.padabajka.navigation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.feature.auth.presentation.LoginComponent
import com.fp.padabajka.feature.auth.presentation.RegisterComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class UnauthScopeNavigateComponent(
    context: ComponentContext
) : NavigateComponentContext<UnauthScopeNavigateComponent.Configuration, UnauthScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.LoginScreen
),
    KoinComponent {

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.LoginScreen -> Child.LoginScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigate(Configuration.RegisterScreen) }
                    )
                }
            )

            Configuration.RegisterScreen -> Child.RegisterScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigate(Configuration.LoginScreen) }
                    )
                }
            )
        }
    }

    sealed interface Child {
        data class LoginScreen(val component: LoginComponent) : Child
        data class RegisterScreen(val component: RegisterComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object LoginScreen : Configuration

        @Serializable
        data object RegisterScreen : Configuration
    }
}
