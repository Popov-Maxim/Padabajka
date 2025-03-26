package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.feature.profile.presentation.ProfileScreenComponent
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreenComponent
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class AuthScopeNavigateComponent(
    context: ComponentContext
) : NavigateComponentContext<AuthScopeNavigateComponent.Configuration, AuthScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.SwiperScreen
),
    KoinComponent {

    fun openSwiper() {
        navigate(Configuration.SwiperScreen)
    }

    fun openProfile() {
        navigate(Configuration.ProfileScreen)
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SwiperScreen -> Child.SwiperScreen(
                component = get { parametersOf(context) }
            )

            Configuration.ProfileScreen -> Child.ProfileScreen(
                component = get {
                    parametersOf(
                        context,
                        { navigate(Configuration.ProfileEditorScreen) }
                    )
                }
            )

            Configuration.ProfileEditorScreen -> Child.ProfileEditorScreen(
                component = get {
                    parametersOf(context)
                }
            )
        }
    }

    sealed interface Child {
        data class ProfileEditorScreen(val component: ProfileEditorScreenComponent) : Child

        sealed interface MainScreen : Child
        data class SwiperScreen(val component: SwiperScreenComponent) : MainScreen
        class ProfileScreen(val component: ProfileScreenComponent) : MainScreen
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SwiperScreen : Configuration

        @Serializable
        data object ProfileScreen : Configuration

        @Serializable
        data object ProfileEditorScreen : Configuration
    }
}
