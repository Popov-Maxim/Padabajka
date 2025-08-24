package com.padabajka.dating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.FpsMonitor
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.datapush.SharedPushHandler
import com.padabajka.dating.feature.auth.presentation.screen.EmailLoginMethodScreen
import com.padabajka.dating.feature.auth.presentation.screen.LoginMethodsScreen
import com.padabajka.dating.feature.auth.presentation.screen.LoginScreen
import com.padabajka.dating.feature.auth.presentation.screen.VerificationScreen
import com.padabajka.dating.feature.push.notification.NotificationService
import com.padabajka.dating.navigation.AuthStateObserverComponent
import com.padabajka.dating.navigation.UnauthScopeNavigateComponent
import com.padabajka.dating.ui.AuthScopeScreen
import org.koin.compose.getKoin
import org.koin.compose.koinInject

@Composable
fun App(rootContext: ComponentContext) {
    InitApp()
    val koin = getKoin()
    val rootComponent = remember {
        AuthStateObserverComponent(rootContext, koin.get(), koin.get(), koin.get())
    }
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

    LoginScreen {
        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            val instance = child.instance

            when (instance) {
                is UnauthScopeNavigateComponent.Child.LoginMethodsScreen ->
                    LoginMethodsScreen(instance.component)
                is UnauthScopeNavigateComponent.Child.EmailLoginMethodScreen ->
                    EmailLoginMethodScreen(instance.component)
            }
        }
    }
}

@Composable
private fun PadabajkaTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        Box(Modifier.background(CoreColors.background.mainColor).fillMaxSize()) {
            content.invoke()
            FpsMonitor(modifier = Modifier.align(Alignment.TopStart))
        }
    }
}

@Composable
private fun InitApp() {
    val notificationService: NotificationService = koinInject()

    LaunchedEffect(Unit) {
        notificationService.initNotificationChannels()
        SharedPushHandler.saveLocalToken()
    }
}
