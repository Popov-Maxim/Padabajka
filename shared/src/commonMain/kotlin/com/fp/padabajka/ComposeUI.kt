package com.fp.padabajka

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.auth.presentation.screen.LoginScreen
import com.fp.padabajka.feature.auth.presentation.screen.RegisterScreen
import com.fp.padabajka.feature.swiper.presentation.screen.SwiperScreen

@Composable
fun App(rootContext: ComponentContext) {
    val rootComponent = NavigateComponentContext(rootContext)
    NavigateApp(rootComponent)
}

@Composable
fun NavigateApp(rootContext: NavigateComponentContext) {
    MaterialTheme {
        val childStack by rootContext.childStack.subscribeAsState()

        val authStateObserver = rememberAuthStateObserver(
            onLogin = { logIn(rootContext) },
            onLogout = { logOut(rootContext) }
        )
        LaunchedEffect(authStateObserver) {
            authStateObserver.subscribeToAuth()
        }

        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when (val instance = child.instance) {
                NavigateComponentContext.Child.SplashScreen -> SplashScreen()
                is NavigateComponentContext.Child.SwiperScreen -> SwiperScreen(instance.component)
                is NavigateComponentContext.Child.LoginScreen -> LoginScreen(instance.component)
                is NavigateComponentContext.Child.RegisterScreen -> RegisterScreen(instance.component)
            }
        }
    }
}

private fun logIn(navigateComponent: NavigateComponentContext) {
    navigateComponent.navigateNewStack(NavigateComponentContext.Configuration.SwiperScreen)
}

private fun logOut(navigateComponent: NavigateComponentContext) {
    navigateComponent.navigateNewStack(NavigateComponentContext.Configuration.LoginScreen)
}
