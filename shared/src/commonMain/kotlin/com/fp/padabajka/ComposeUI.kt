package com.fp.padabajka

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.messenger.presentation.MessengerScreen
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

        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when (val instance = child.instance) {
                is NavigateComponentContext.Child.SwiperScreen -> SwiperScreen(instance.component)
                is NavigateComponentContext.Child.MessengerScreen -> MessengerScreen(instance.component)
            }
        }
    }
}
