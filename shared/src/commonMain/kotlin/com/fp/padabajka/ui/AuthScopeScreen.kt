package com.fp.padabajka.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.profile.presentation.ProfileScreen
import com.fp.padabajka.feature.profile.presentation.editor.ProfileEditorScreen
import com.fp.padabajka.feature.swiper.presentation.screen.SwiperScreen
import com.fp.padabajka.navigation.AuthScopeNavigateComponent

@Composable
fun AuthScopeScreen(component: AuthScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->

        Column {
            val instance = child.instance
            Box(modifier = Modifier.weight(1f)) {
                when (instance) {
                    is AuthScopeNavigateComponent.Child.ProfileScreen -> ProfileScreen(instance.component)
                    is AuthScopeNavigateComponent.Child.SwiperScreen -> SwiperScreen(instance.component)
                    is AuthScopeNavigateComponent.Child.ProfileEditorScreen -> ProfileEditorScreen(
                        instance.component
                    )
                }
            }

            if (instance is AuthScopeNavigateComponent.Child.MainScreen) {
                NavigateBar(
                    openSwiper = component::openSwiper,
                    openProfile = component::openProfile
                )
            }
        }
    }
}
