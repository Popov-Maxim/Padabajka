package com.padabajka.dating.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.feature.profile.presentation.ProfileScreen
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreen
import com.padabajka.dating.feature.swiper.presentation.screen.SwiperScreen
import com.padabajka.dating.navigation.AuthScopeNavigateComponent

@Composable
fun AuthScopeScreen(component: AuthScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()
    val navigateBar = @Composable {
        NavigateBar(
            openSwiper = component::openSwiper,
            openProfile = component::openProfile
        )
    }

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            is AuthScopeNavigateComponent.Child.ProfileScreen ->
                ProfileScreen(instance.component, navigateBar)

            is AuthScopeNavigateComponent.Child.SwiperScreen ->
                SwiperScreen(instance.component, navigateBar)

            is AuthScopeNavigateComponent.Child.ProfileEditorScreen ->
                ProfileEditorScreen(instance.component)
        }
    }
}
