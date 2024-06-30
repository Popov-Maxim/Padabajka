package com.fp.padabajka

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.fp.padabajka.core.repository.api.model.messenger.ChatId
import com.fp.padabajka.feature.auth.presentation.LoginComponent
import com.fp.padabajka.feature.auth.presentation.RegisterComponent
import com.fp.padabajka.feature.messenger.presentation.MessengerComponent
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class NavigateComponentContext(
    context: ComponentContext
) : ComponentContext by context, KoinComponent {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.RegisterScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun navigate(configuration: Configuration) {
        navigation.navigate {
            it - configuration + configuration
        }
    }

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SwiperScreen -> Child.SwiperScreen(
                component = get { parametersOf(context) }
            )

            is Configuration.MessengerScreen -> Child.MessengerScreen(
                component = get { parametersOf(context, configuration.chatId) }
            )

            Configuration.LoginScreen -> Child.LoginScreen(
                component = get { parametersOf(context, { navigate(Configuration.RegisterScreen) }) }
            )

            Configuration.RegisterScreen -> Child.RegisterScreen(
                component = get { parametersOf(context, { navigate(Configuration.LoginScreen) }) }
            )
        }
    }

    sealed interface Child {
        data class SwiperScreen(val component: SwiperScreenComponent) : Child
        data class MessengerScreen(val component: MessengerComponent) : Child
        data class LoginScreen(val component: LoginComponent) : Child
        data class RegisterScreen(val component: RegisterComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object SwiperScreen : Configuration

        @Serializable
        data class MessengerScreen(val chatId: ChatId) : Configuration
        data object LoginScreen : Configuration
        data object RegisterScreen : Configuration
    }
}
