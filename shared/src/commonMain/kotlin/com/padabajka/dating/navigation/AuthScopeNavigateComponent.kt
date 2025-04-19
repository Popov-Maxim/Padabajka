package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.presentation.MessengerComponent
import com.padabajka.dating.feature.messenger.presentation.chat.ChatComponent
import com.padabajka.dating.feature.profile.presentation.ProfileScreenComponent
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreenComponent
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import com.padabajka.dating.settings.presentation.SettingScreenComponent
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

    fun openSettings() {
        navigate(Configuration.SettingScreen)
    }

    fun openMessenger() {
        navigate(Configuration.MessengerScreen)
    }

    fun openChat(chatId: ChatId) {
        navigate(Configuration.ChatScreen(chatId))
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
                    parametersOf(
                        context,
                        ::navigateBack
                    )
                }
            )

            Configuration.SettingScreen -> Child.SettingScreen(
                component = get {
                    parametersOf(
                        context,
                        ::navigateBack
                    )
                }
            )

            is Configuration.ChatScreen -> Child.ChatScreen(
                component = get { parametersOf(context, configuration.chatId) }
            )

            Configuration.MessengerScreen -> Child.MessengerScreen(
                component = get {
                    parametersOf(
                        context,
                        { chatId: ChatId -> navigate(Configuration.ChatScreen(chatId)) }
                    )
                }
            )
        }
    }

    sealed interface Child {
        data class ProfileEditorScreen(val component: ProfileEditorScreenComponent) : Child
        data class SettingScreen(val component: SettingScreenComponent) : Child

        data class SwiperScreen(val component: SwiperScreenComponent) : Child
        data class ProfileScreen(val component: ProfileScreenComponent) : Child
        data class ChatScreen(val component: ChatComponent) : Child
        data class MessengerScreen(val component: MessengerComponent) : Child
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SwiperScreen : Configuration

        @Serializable
        data object ProfileScreen : Configuration

        @Serializable
        data object ProfileEditorScreen : Configuration

        @Serializable
        data object SettingScreen : Configuration

        @Serializable
        data object MessengerScreen : Configuration

        @Serializable
        data class ChatScreen(val chatId: ChatId) : Configuration
    }
}
