package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.feature.auth.presentation.EmailLoginMethodComponent
import com.padabajka.dating.feature.auth.presentation.LoginMethodsComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class UnauthScopeNavigateComponent(
    context: ComponentContext
) : NavigateComponentContext<UnauthScopeNavigateComponent.Configuration, UnauthScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.LoginMethodsScreen
),
    KoinComponent {

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.LoginMethodsScreen -> Child.LoginMethodsScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigate(Configuration.EmailLoginMethodScreen) }
                    )
                }
            )

            Configuration.EmailLoginMethodScreen -> Child.EmailLoginMethodScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigateBack() }
                    )
                }
            )
        }
    }

    sealed interface Child {
        data class LoginMethodsScreen(val component: LoginMethodsComponent) : Child
        data class EmailLoginMethodScreen(val component: EmailLoginMethodComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object LoginMethodsScreen : Configuration

        @Serializable
        data object EmailLoginMethodScreen : Configuration
    }
}
