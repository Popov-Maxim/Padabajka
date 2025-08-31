package com.padabajka.dating.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.feature.profile.presentation.creator.birthday.CreateProfileBirthdayScreen
import com.padabajka.dating.feature.profile.presentation.creator.name.CreateProfileNameScreen
import com.padabajka.dating.feature.profile.presentation.creator.welcome.WelcomeScreen
import com.padabajka.dating.navigation.CreateProfileScopeNavigateComponent

@Composable
fun CreateProfileScopeScreen(component: CreateProfileScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()
    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        val instance = child.instance
        when (instance) {
            is CreateProfileScopeNavigateComponent.Child.WelcomeScreen -> WelcomeScreen(instance.toNext)
            is CreateProfileScopeNavigateComponent.Child.NameScreen -> CreateProfileNameScreen(instance.component)
            is CreateProfileScopeNavigateComponent.Child.BirthdayScreen ->
                CreateProfileBirthdayScreen(instance.component)
            CreateProfileScopeNavigateComponent.Child.SexAndPreferencesScreen -> Unit
            CreateProfileScopeNavigateComponent.Child.LookingForScreen -> Unit
            CreateProfileScopeNavigateComponent.Child.ImageScreen -> Unit
        }
    }
}
