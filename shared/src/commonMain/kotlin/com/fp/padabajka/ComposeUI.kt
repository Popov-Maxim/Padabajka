package com.fp.padabajka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.core.presentation.ui.CoreColors
import com.fp.padabajka.core.presentation.ui.mainColor
import com.fp.padabajka.feature.auth.presentation.screen.LoginScreen
import com.fp.padabajka.feature.auth.presentation.screen.RegisterScreen
import com.fp.padabajka.feature.auth.presentation.screen.VerificationScreen
import com.fp.padabajka.navigation.AuthStateObserverComponent
import com.fp.padabajka.navigation.UnauthScopeNavigateComponent
import com.fp.padabajka.ui.AuthScopeScreen

@Composable
fun App(rootContext: ComponentContext) {
    val rootComponent = remember { AuthStateObserverComponent(rootContext) }
    NavigateApp(rootComponent)
}

@Composable
private fun NavigateApp(rootContext: AuthStateObserverComponent) {
    PadabajkaTheme {
        val childStack by rootContext.childStack.subscribeAsState()

        LaunchedEffect(rootContext) {
            rootContext.subscribeToAuth()
        }

        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            val instance = child.instance
            when (instance) {
                is AuthStateObserverComponent.Child.AuthScope -> AuthScopeScreen(instance.component)
                AuthStateObserverComponent.Child.SplashScreen -> SplashScreen()
                is AuthStateObserverComponent.Child.UnauthScope -> UnauthScopeScreen(instance.component)
                is AuthStateObserverComponent.Child.VerificationScreen -> VerificationScreen(instance.component)
            }
        }
    }
}

@Composable
private fun UnauthScopeScreen(component: UnauthScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance

        when (instance) {
            is UnauthScopeNavigateComponent.Child.LoginScreen -> LoginScreen(instance.component)
            is UnauthScopeNavigateComponent.Child.RegisterScreen -> RegisterScreen(instance.component)
        }
    }
}

@Composable
private fun PadabajkaTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        Box(Modifier.background(CoreColors.background.mainColor).fillMaxSize()) {
            content.invoke()
        }
    }
}
