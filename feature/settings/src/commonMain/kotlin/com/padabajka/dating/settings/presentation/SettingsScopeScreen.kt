package com.padabajka.dating.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.feature.permission.flow.presentation.PermissionFlowScreen
import com.padabajka.dating.settings.presentation.setting.LanguageSelectorScreen

@Composable
fun SettingsScopeScreen(component: SettingsScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            is SettingsScopeNavigateComponent.Child.LanguageSelectorScreen ->
                LanguageSelectorScreen(instance.component)
            is SettingsScopeNavigateComponent.Child.MainSettingScreen ->
                SettingScreen(instance.component)
            is SettingsScopeNavigateComponent.Child.PermissionFlowScreen ->
                PermissionFlowScreen(instance.component)
        }
    }
}
