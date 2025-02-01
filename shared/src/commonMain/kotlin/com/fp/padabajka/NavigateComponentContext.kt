package com.fp.padabajka

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.fp.padabajka.feature.auth.presentation.LoginComponent
import com.fp.padabajka.feature.auth.presentation.RegisterComponent
import com.fp.padabajka.feature.profile.presentation.ProfileScreenComponent
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
        initialConfiguration = Configuration.SplashScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun navigate(configuration: Configuration) {
        navigation.navigate {
            it - Configuration.SplashScreen - configuration + configuration
        }
    }

    fun navigateNewStack(configuration: Configuration) {
        navigation.navigate {
            listOf(configuration)
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

            Configuration.LoginScreen -> Child.LoginScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigate(Configuration.RegisterScreen) }
                    )
                }
            )

            Configuration.RegisterScreen -> Child.RegisterScreen(
                component = get { parametersOf(context, { navigate(Configuration.LoginScreen) }) }
            )

            Configuration.SplashScreen -> Child.SplashScreen
            Configuration.ProfileScreen -> Child.ProfileScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigate(Configuration.ProfileEditorScreen) }
                    )
                }
            )

            Configuration.ProfileEditorScreen -> Child.ProfileEditorScreen
        }
    }

    sealed interface Child {
        data object SplashScreen : Child
        data class LoginScreen(val component: LoginComponent) : Child
        data class RegisterScreen(val component: RegisterComponent) : Child

        data object ProfileEditorScreen : Child

        sealed interface MainScreen : Child
        data class SwiperScreen(val component: SwiperScreenComponent) : MainScreen
        class ProfileScreen(val component: ProfileScreenComponent) : MainScreen
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SplashScreen : Configuration

        @Serializable
        data object SwiperScreen : Configuration

        @Serializable
        data object LoginScreen : Configuration

        @Serializable
        data object RegisterScreen : Configuration

        @Serializable
        data object ProfileScreen : Configuration

        @Serializable
        data object ProfileEditorScreen : Configuration
    }
}
