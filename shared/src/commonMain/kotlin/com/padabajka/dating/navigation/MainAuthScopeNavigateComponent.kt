package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.presentation.MessengerComponent
import com.padabajka.dating.feature.messenger.presentation.chat.ChatComponent
import com.padabajka.dating.feature.messenger.presentation.model.PersonItem
import com.padabajka.dating.feature.permission.flow.presentation.PermissionFlowComponent
import com.padabajka.dating.feature.profile.presentation.ProfileScreenComponent
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreenComponent
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import com.padabajka.dating.settings.presentation.SettingScreenComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class MainAuthScopeNavigateComponent(
    context: ComponentContext,
    private val userId: UserId
) : NavigateComponentContext<MainAuthScopeNavigateComponent.Configuration, MainAuthScopeNavigateComponent.Child>(
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

    @Suppress("LongMethod")
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
                        ::navigateBack,
                        { navigate(Configuration.PermissionFlowScreen) }
                    )
                }
            )

            is Configuration.ChatScreen -> Child.ChatScreen(
                component = get {
                    parametersOf(
                        context,
                        configuration.chatId,
                        configuration.personItem,
                        userId,
                        ::navigateBack
                    )
                }
            )

            Configuration.MessengerScreen -> Child.MessengerScreen(
                component = get {
                    parametersOf(
                        context,
                        { chatId: ChatId, personItem: PersonItem ->
                            navigate(Configuration.ChatScreen(chatId, personItem))
                        }
                    )
                }
            )

            Configuration.PermissionFlowScreen -> Child.PermissionFlowScreen(
                component = get {
                    parametersOf(
                        context,
                        ::navigateBack
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

        data class PermissionFlowScreen(val component: PermissionFlowComponent) : Child
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
        data class ChatScreen(
            val chatId: ChatId,
            val personItem: PersonItem
        ) : Configuration

        @Serializable
        data object PermissionFlowScreen : Configuration
    }
}
