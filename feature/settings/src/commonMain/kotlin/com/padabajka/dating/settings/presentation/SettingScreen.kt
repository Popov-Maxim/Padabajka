package com.padabajka.dating.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.permission.NotificationPermissionController
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.SimpleConfirmDialog
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.layout.SimpleTopBar
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.utils.isDebugBuild
import com.padabajka.dating.settings.presentation.model.DeleteAccountEvent
import com.padabajka.dating.settings.presentation.model.FreezeAccountEvent
import com.padabajka.dating.settings.presentation.model.LogOutEvent
import com.padabajka.dating.settings.presentation.model.NavigateBackEvent
import com.padabajka.dating.settings.presentation.model.OpenLanguageSelectorEvent
import com.padabajka.dating.settings.presentation.model.RequestPermissionEvent
import com.padabajka.dating.settings.presentation.model.SettingsEvent
import com.padabajka.dating.settings.presentation.model.UnfreezeAccountEvent
import com.padabajka.dating.settings.presentation.model.language.supportedLanguagesMap
import com.padabajka.dating.settings.presentation.setting.AppSettingsDialog
import org.koin.compose.koinInject

@Composable
fun SettingScreen(component: SettingScreenComponent) {
    CustomScaffold(
        topBar = { TopBar(component::onEvent) }
    ) {
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 50.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GeneralSetting(component)
            LittleSetting(component)
        }
    }
}

@Composable
private fun GeneralSetting(
    component: SettingScreenComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()

    var showDialog by remember { mutableStateOf(false) }
    val notificationPermissionController: NotificationPermissionController = koinInject()
    val initPermissionAllow by produceState<Boolean?>(initialValue = null) {
        value = notificationPermissionController.hasPermission()
    }
    var permissionAllow by remember(initPermissionAllow) { mutableStateOf(initPermissionAllow) }

    var showLogoutDialog: Boolean by remember { mutableStateOf(false) }
    var showFreezingDialog: Boolean by remember { mutableStateOf(false) }

    val settingsButtonData = listOfNotNull(
        SettingButtonData(
            iconData = Icons.Filled.Settings.toData(),
            text = "AppSettings",
            secondText = null,
            onClick = { showDialog = true }
        ).takeIf { isDebugBuild },
        SettingButtonData(
            iconData = IconData.Empty,
            text = StaticTextId.UiId.Subscription.translate(),
            secondText = if (state.subscriptionActive) {
                StaticTextId.UiId.SubscriptionActive.translate()
            } else {
                StaticTextId.UiId.SubscriptionInactive.translate()
            },
            onClick = { component.onEvent(SettingsEvent.OpenSubscription) }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Language.toData(),
            text = StaticTextId.UiId.Language.translate(),
            secondText = state.selectedLanguage.uiText(),
            onClick = { component.onEvent(OpenLanguageSelectorEvent) }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Notification.toData(),
            text = StaticTextId.UiId.Notification.translate(),
            secondText = "permissionAllow: $permissionAllow", // TODO(P0)
            onClick = { component.onEvent(RequestPermissionEvent) }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.FAQ.toData(),
            text = StaticTextId.UiId.FAQ.translate(),
            secondText = null,
            onClick = { }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Snowman.toData(),
            text = StaticTextId.UiId.FreezeProfile.translate(),
            secondText = if (state.profileFrozen) {
                StaticTextId.UiId.ProfileFrozen.translate()
            } else {
                StaticTextId.UiId.ProfileActive.translate()
            },
            onClick = { showFreezingDialog = true }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Logout.toData(),
            text = StaticTextId.UiId.LogOut.translate(),
            secondText = null,
            onClick = { showLogoutDialog = true }
        ),

//            SettingButtonData(
//                iconData = IconData.Empty,
//                text = "refresh push token",
//                onClick = { component.onEvent(SendPushToken) }
//            ),
//            SettingButtonData(
//                iconData = IconData.Empty,
//                text = "sync data",
//                onClick = { component.onEvent(SyncData) }
//            ),
    )
    Column(
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = StaticTextId.UiId.General.translate(),
                fontSize = 24.sp
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                settingsButtonData.onEach { data ->
                    SettingButton(data.iconData, data.text, data.secondText, data.onClick)
                }
            }
        }
    }

    if (showDialog) {
        AppSettingsDialog { showDialog = false }
    }

    if (showLogoutDialog) {
        SimpleConfirmDialog(
            text = StaticTextId.UiId.LogoutAlertDialogText.translate(),
            confirmText = StaticTextId.UiId.Yes.translate(),
            onConfirm = { component.onEvent(LogOutEvent) },
            dismissText = StaticTextId.UiId.No.translate(),
            onDismiss = { showLogoutDialog = false },
        )
    }

    if (showFreezingDialog) {
        SimpleConfirmDialog(
            text = if (state.profileFrozen) {
                StaticTextId.UiId.UnfreezeAccountAlertDialogText.translate()
            } else {
                StaticTextId.UiId.FreezeAccountAlertDialogText.translate()
            },
            confirmText = StaticTextId.UiId.Yes.translate(),
            onConfirm = {
                if (state.profileFrozen) {
                    component.onEvent(UnfreezeAccountEvent)
                } else {
                    component.onEvent(FreezeAccountEvent)
                }
                showFreezingDialog = false
            },
            dismissText = StaticTextId.UiId.No.translate(),
            onDismiss = { showFreezingDialog = false },
        )
    }
}

@Composable
private fun LittleSetting(
    component: SettingScreenComponent,
    modifier: Modifier = Modifier
) {
    var showDeletingDialog: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        LittleButton(
            text = StaticTextId.UiId.TermsOfUse.translate()
        )
        LittleButton(
            text = StaticTextId.UiId.DeleteAccount.translate(),
            onClick = { showDeletingDialog = true }
        )
    }

    if (showDeletingDialog) {
        SimpleConfirmDialog(
            text = StaticTextId.UiId.DeleteAccountAlertDialogText.translate(),
            confirmText = StaticTextId.UiId.Yes.translate(),
            onConfirm = { component.onEvent(DeleteAccountEvent) },
            dismissText = StaticTextId.UiId.No.translate(),
            onDismiss = { showDeletingDialog = false },
        )
    }
}

@Composable
private fun LittleButton(
    text: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
//            .background(Color.LightGray)
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}

private data class SettingButtonData(
    val iconData: IconData,
    val text: String,
    val secondText: String? = null,
    val onClick: () -> Unit
)

@Composable
private fun SettingButton(
    iconData: IconData,
    text: String,
    secondText: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    iconData = iconData,
                    modifier = Modifier.size(30.dp),
                    contentDescription = null
                )

                Column {
                    Text(text = text)
                    if (secondText != null) {
                        Text(
                            text = secondText,
                            color = Color(color = 0xFF7E7D7D)
                        )
                    }
                }
            }
            Icon(
                painter = CoreIcons.RightArrow,
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TopBar(onEvent: (SettingsEvent) -> Unit) {
    SimpleTopBar(
        title = StaticTextId.UiId.Settings.translate(),
        navigateBack = { onEvent(NavigateBackEvent) }
    )
}

private fun Language.Static.uiText(): String {
    return supportedLanguagesMap[this]!!.name
}
