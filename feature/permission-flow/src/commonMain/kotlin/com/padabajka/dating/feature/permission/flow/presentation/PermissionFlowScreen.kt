package com.padabajka.dating.feature.permission.flow.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCircularProgressIndicator

@Composable
fun PermissionFlowScreen(component: PermissionFlowComponent) {
    val childStack by component.childStack.subscribeAsState()
    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            is PermissionFlowComponent.Child.GeoPermissionScreen -> GeoPermissionScreen(instance.permissionComponent)
            is PermissionFlowComponent.Child.NotificationPermissionScreen ->
                NotificationPermissionScreen(instance.permissionComponent)
            PermissionFlowComponent.Child.SplashScreen -> SplashScreen("Permission Flow")
            is PermissionFlowComponent.Child.FinishScreen -> FinishScreen(instance.onApply)
        }
    }
}

@Composable
fun SplashScreen(
    loadingMessage: String
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp, alignment = Alignment.CenterVertically)
    ) {
        CoreCircularProgressIndicator()
        Text(
            text = loadingMessage,
            fontSize = 20.sp
        )
    }
}
