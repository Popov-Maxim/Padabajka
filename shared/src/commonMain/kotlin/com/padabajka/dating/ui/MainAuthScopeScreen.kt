package com.padabajka.dating.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.feature.messenger.presentation.MessengerScreen
import com.padabajka.dating.feature.messenger.presentation.chat.ChatScreen
import com.padabajka.dating.feature.permission.flow.presentation.PermissionFlowScreen
import com.padabajka.dating.feature.profile.presentation.ProfileScreen
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreen
import com.padabajka.dating.feature.swiper.presentation.screen.SwiperScreen
import com.padabajka.dating.navigation.MainAuthScopeNavigateComponent
import com.padabajka.dating.settings.presentation.SettingScreen

@Composable
fun MainAuthScopeScreen(component: MainAuthScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()
    val navigateBar = @Composable {
        NavigateBar(
            openSwiper = component::openSwiper,
            openProfile = component::openProfile,
            openMessenger = component::openMessenger
        )
    }

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            is MainAuthScopeNavigateComponent.Child.ProfileScreen ->
                ProfileScreen(instance.component, navigateBar) {
                    component.openSettings()
                }

            is MainAuthScopeNavigateComponent.Child.SwiperScreen ->
                SwiperScreen(instance.component, navigateBar)

            is MainAuthScopeNavigateComponent.Child.ProfileEditorScreen ->
                ProfileEditorScreen(instance.component)

            is MainAuthScopeNavigateComponent.Child.SettingScreen ->
                SettingScreen(instance.component)

            is MainAuthScopeNavigateComponent.Child.ChatScreen ->
                ChatScreen(instance.component)

            is MainAuthScopeNavigateComponent.Child.MessengerScreen ->
                MessengerScreen(instance.component, navigateBar)

            is MainAuthScopeNavigateComponent.Child.PermissionFlowScreen ->
                PermissionFlowScreen(instance.component)
        }
    }
}
