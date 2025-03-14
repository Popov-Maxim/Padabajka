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
import androidx.compose.runtime.remember
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
import com.fp.padabajka.feature.auth.presentation.screen.VerificationScreen
import com.fp.padabajka.feature.profile.presentation.ProfileScreen
import com.fp.padabajka.feature.profile.presentation.editor.ProfileEditorScreen
import com.fp.padabajka.feature.swiper.presentation.screen.SwiperScreen
import com.fp.padabajka.navigation.AuthScopeNavigateComponent
import com.fp.padabajka.navigation.AuthStateObserverComponent
import com.fp.padabajka.navigation.UnauthScopeNavigateComponent

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
private fun AuthScopeScreen(component: AuthScopeNavigateComponent) {
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

@Composable
private fun NavigateBar(
    openSwiper: () -> Unit,
    openProfile: () -> Unit
) {
    Row(modifier = Modifier.height(65.dp).background(Color.White)) {
        ScreenContent(
            modifier = Modifier.weight(1f).clickable {
                openSwiper()
            },
            name = "Swiper"
        )
        ScreenContent(
            modifier = Modifier.weight(1f).clickable {
                openProfile()
            },
            name = "Profile"
        )
    }
}

@Composable
private fun ScreenContent(modifier: Modifier, name: String) {
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
        Box(Modifier.background(Color.LightGray).fillMaxSize()) {
            content.invoke()
        }
    }
}
