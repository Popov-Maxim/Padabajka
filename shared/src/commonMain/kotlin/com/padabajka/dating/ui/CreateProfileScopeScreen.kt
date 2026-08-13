package com.padabajka.dating.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.SimpleConfirmDialog
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.image.presentation.ImageCropScreen
import com.padabajka.dating.feature.profile.presentation.creator.birthday.CreateProfileBirthdayScreen
import com.padabajka.dating.feature.profile.presentation.creator.finish.CreateProfileFinishScreen
import com.padabajka.dating.feature.profile.presentation.creator.gender.CreateProfileSexScreen
import com.padabajka.dating.feature.profile.presentation.creator.image.CreateProfileImageScreen
import com.padabajka.dating.feature.profile.presentation.creator.lookingfor.CreateProfileLookingForDetailSelectorScreen
import com.padabajka.dating.feature.profile.presentation.creator.lookingfor.CreateProfileLookingForTypeSelectorScreen
import com.padabajka.dating.feature.profile.presentation.creator.name.CreateProfileNameScreen
import com.padabajka.dating.feature.profile.presentation.creator.welcome.WelcomeScreen
import com.padabajka.dating.navigation.CreateProfileScopeNavigateComponent

@Composable
fun CreateProfileScopeScreen(component: CreateProfileScopeNavigateComponent) {
    val childStack by component.childStack.subscribeAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Children(
            stack = childStack,
            animation = NavigateComponentContext.defaultAnimation()
        ) { child ->
            val instance = child.instance
            when (instance) {
                is CreateProfileScopeNavigateComponent.Child.WelcomeScreen -> WelcomeScreen(instance.toNext)
                is CreateProfileScopeNavigateComponent.Child.NameScreen -> CreateProfileNameScreen(instance.component)
                is CreateProfileScopeNavigateComponent.Child.BirthdayScreen ->
                    CreateProfileBirthdayScreen(instance.component)
                is CreateProfileScopeNavigateComponent.Child.SexAndPreferencesScreen ->
                    CreateProfileSexScreen(instance.component)
                is CreateProfileScopeNavigateComponent.Child.TypeLookingForScreen ->
                    CreateProfileLookingForTypeSelectorScreen(instance.onTypeSelected)
                is CreateProfileScopeNavigateComponent.Child.DetailLookingForScreen ->
                    CreateProfileLookingForDetailSelectorScreen(instance.component)

                is CreateProfileScopeNavigateComponent.Child.ImageScreen ->
                    CreateProfileImageScreen(instance.component)

                is CreateProfileScopeNavigateComponent.Child.ImageCropScreen ->
                    ImageCropScreen(instance.component)

                is CreateProfileScopeNavigateComponent.Child.FinishScreen ->
                    CreateProfileFinishScreen(instance.component)
            }
        }

        if (childStack.active.instance !is CreateProfileScopeNavigateComponent.Child.ImageCropScreen) {
            TextButton(
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                onClick = { showLogoutDialog = true }
            ) {
                Text(
                    text = StaticTextId.UiId.LogOut.translate(),
                    color = CoreColors.background.textColor
                )
            }
        }
    }

    if (showLogoutDialog) {
        SimpleConfirmDialog(
            text = StaticTextId.UiId.LogoutAlertDialogText.translate(),
            confirmText = StaticTextId.UiId.Yes.translate(),
            onConfirm = component::logout,
            dismissText = StaticTextId.UiId.No.translate(),
            onDismiss = { showLogoutDialog = false },
        )
    }
}
