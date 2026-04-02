package com.padabajka.dating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.FpsMonitor
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.hideKeyboardOnTap
import com.padabajka.dating.core.presentation.ui.toDp
import com.padabajka.dating.datapush.SharedPushHandler
import com.padabajka.dating.feature.auth.presentation.screen.DebugLoginMethodScreen
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
        AuthStateObserverComponent(rootContext, koin.get(), koin.get())
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
            animation = NavigateComponentContext.defaultAnimation()
        ) { child ->
            val instance = child.instance
            when (instance) {
                is AuthStateObserverComponent.Child.AuthScope -> AuthScopeScreen(instance.component)
                AuthStateObserverComponent.Child.SplashScreen -> SplashScreen("Auth State")
                is AuthStateObserverComponent.Child.UnauthScope -> UnauthScopeScreen(instance.component)
                is AuthStateObserverComponent.Child.VerificationScreen -> VerificationScreen(
                    instance.component
                )
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
            animation = NavigateComponentContext.defaultAnimation()
        ) { child ->
            val instance = child.instance

            when (instance) {
                is UnauthScopeNavigateComponent.Child.LoginMethodsScreen ->
                    LoginMethodsScreen(instance.component)

                is UnauthScopeNavigateComponent.Child.EmailLoginMethodScreen ->
                    EmailLoginMethodScreen(instance.component)

                is UnauthScopeNavigateComponent.Child.DebugLoginMethodScreen ->
                    DebugLoginMethodScreen(instance.component)
            }
        }
    }
}

@Composable
private fun PadabajkaTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(CoreColors.background.mainColor)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
                    .hideKeyboardOnTap()
            ) {
                content.invoke()
                FpsMonitor(modifier = Modifier.align(Alignment.TopStart))
            }
            val density = LocalDensity.current
            val navBarHeightPx = WindowInsets.navigationBars.getBottom(density)
            val imeBottomPx = WindowInsets.ime.getBottom(density)

            val targetHeightPx = (navBarHeightPx - imeBottomPx).coerceAtLeast(0)

            if (targetHeightPx > 0) {
                val animatedHeightDp = targetHeightPx.toDp()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeightDp)
                        .background(CoreColors.background.mainColor)
                )
            }
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
