package com.padabajka.dating.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.presentation.chat.ChatScreen
import com.padabajka.dating.feature.profile.presentation.ProfileScreen
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreen
import com.padabajka.dating.feature.swiper.presentation.screen.SwiperScreen
import com.padabajka.dating.navigation.AuthScopeNavigateComponent
import com.padabajka.dating.settings.presentation.SettingScreen

@Composable
fun AuthScopeScreen(component: AuthScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()
    val navigateBar = @Composable {
        NavigateBar(
            openSwiper = component::openSwiper,
            openProfile = component::openProfile,
            openMessenger = { component.openChat(ChatId("1")) }
        )
    }

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            is AuthScopeNavigateComponent.Child.ProfileScreen ->
                ProfileScreen(instance.component, navigateBar) {
                    component.openSettings()
                }

            is AuthScopeNavigateComponent.Child.SwiperScreen ->
                SwiperScreen(instance.component, navigateBar)

            is AuthScopeNavigateComponent.Child.ProfileEditorScreen ->
                ProfileEditorScreen(instance.component)

            is AuthScopeNavigateComponent.Child.SettingScreen ->
                SettingScreen(instance.component)

            is AuthScopeNavigateComponent.Child.ChatScreen ->
                ChatScreen(instance.component)
        }
    }
}
