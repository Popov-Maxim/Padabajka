package com.padabajka.dating.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.SplashScreen
import com.padabajka.dating.navigation.AuthScopeNavigateComponent

@Composable
fun AuthScopeScreen(component: AuthScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()
    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            AuthScopeNavigateComponent.Child.LoginScreen -> SplashScreen()
            is AuthScopeNavigateComponent.Child.CreateProfileScope -> CreateProfileScopeScreen(instance.component)
            is AuthScopeNavigateComponent.Child.MainAuthScope -> MainAuthScopeScreen(instance.component)
        }
    }
}
