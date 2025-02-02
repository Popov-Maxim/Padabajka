package com.fp.padabajka

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.auth.presentation.screen.LoginScreen
import com.fp.padabajka.feature.auth.presentation.screen.RegisterScreen
import com.fp.padabajka.feature.profile.presentation.ProfileScreen
import com.fp.padabajka.feature.profile.presentation.editor.ProfileEditorScreen
import com.fp.padabajka.feature.swiper.presentation.screen.SwiperScreen

@Composable
fun App(rootContext: ComponentContext) {
    val rootComponent = NavigateComponentContext(rootContext)
    NavigateApp(rootComponent)
}

@Composable
fun NavigateApp(rootContext: NavigateComponentContext) {
    PadabajkaTheme {
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
            val instance = child.instance
            Column {
                Box(modifier = Modifier.weight(1f)) {
                    when (instance) {
                        NavigateComponentContext.Child.SplashScreen -> SplashScreen()
                        is NavigateComponentContext.Child.SwiperScreen -> SwiperScreen(instance.component)
                        is NavigateComponentContext.Child.LoginScreen -> LoginScreen(instance.component)
                        is NavigateComponentContext.Child.RegisterScreen -> RegisterScreen(instance.component)
                        is NavigateComponentContext.Child.ProfileScreen -> ProfileScreen(instance.component)
                        is NavigateComponentContext.Child.ProfileEditorScreen -> ProfileEditorScreen(instance.component)
                    }
                }

                if (instance is NavigateComponentContext.Child.MainScreen) {
                    Row(modifier = Modifier.height(65.dp).background(Color.White)) {
                        ScreenContent(
                            modifier = Modifier.weight(1f).clickable {
                                rootContext.navigate(
                                    NavigateComponentContext.Configuration.SwiperScreen
                                )
                            },
                            name = "Swiper"
                        )
                        ScreenContent(
                            modifier = Modifier.weight(1f).clickable {
                                rootContext.navigate(
                                    NavigateComponentContext.Configuration.ProfileScreen
                                )
                            },
                            name = "Profile"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenContent(modifier: Modifier, name: String) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Экран: $name",
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun PadabajkaTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        Box(Modifier.background(Color.LightGray)) {
            content.invoke()
        }
    }
}

private fun logIn(navigateComponent: NavigateComponentContext) {
    navigateComponent.navigateNewStack(NavigateComponentContext.Configuration.SwiperScreen)
}

private fun logOut(navigateComponent: NavigateComponentContext) {
    navigateComponent.navigateNewStack(NavigateComponentContext.Configuration.LoginScreen)
}
