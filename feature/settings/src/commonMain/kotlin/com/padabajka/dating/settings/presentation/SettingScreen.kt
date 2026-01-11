package com.padabajka.dating.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.permission.NotificationPermissionController
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.settings.presentation.model.LogOutEvent
import com.padabajka.dating.settings.presentation.model.NavigateBackEvent
import com.padabajka.dating.settings.presentation.model.RequestPermissionEvent
import com.padabajka.dating.settings.presentation.model.SettingsEvent
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
    var showDialog by remember { mutableStateOf(false) }
    val notificationPermissionController: NotificationPermissionController = koinInject()
    val initPermissionAllow by produceState<Boolean?>(initialValue = null) {
        value = notificationPermissionController.hasPermission()
    }
    var permissionAllow by remember(initPermissionAllow) { mutableStateOf(initPermissionAllow) }

    val settingsButtonData = listOf(
//            SettingButtonData(
//                iconData = CoreIcons.NavigationBar.Profile.toData(),
//                text = StaticTextId.UiId.Name.translate(),
//                secondText = null,
//                onClick = {}
//            ),
//            SettingButtonData(
//                iconData = Icons.Filled.Settings.toData(),
//                text = "AppSettings",
//                secondText = null,
//                onClick = { showDialog = true }
//            ),
        SettingButtonData(
            iconData = IconData.Empty,
            text = StaticTextId.UiId.Subscription.translate(),
            secondText = "не активирована",
            onClick = { }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Language.toData(),
            text = StaticTextId.UiId.Language.translate(),
            secondText = "русский",
            onClick = { }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Notification.toData(),
            text = StaticTextId.UiId.Notification.translate(),
            secondText = "permissionAllow: $permissionAllow",
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
            secondText = "account is active",
            onClick = { }
        ),
        SettingButtonData(
            iconData = CoreIcons.Settings.Logout.toData(),
            text = StaticTextId.UiId.LogOut.translate(),
            secondText = null,
            onClick = { component.onEvent(LogOutEvent) }
        ),

//            SettingButtonData(
//                iconData = IconData.Empty,
//                text = "request permission",
//                secondText = "permissionAllow: $permissionAllow",
//                onClick = {
//                    coroutineScope.launch {
//                        component.onEvent(RequestPermissionEvent)
//                    }
//                }
//            ),
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
}

@Suppress("UnusedParameter")
@Composable
private fun LittleSetting(
    component: SettingScreenComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        LittleButton(
            text = StaticTextId.UiId.TermsOfUse.translate()
        )
        LittleButton(
            text = StaticTextId.UiId.DeleteAccount.translate()
        )
    }
}

@Composable
private fun LittleButton(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                        Text(text = secondText)
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
    Box(
        modifier = Modifier.background(CoreColors.background.mainColor)
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEvent(NavigateBackEvent) },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = CoreIcons.BackArrow,
                    contentDescription = "Back",
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = StaticTextId.UiId.Settings.translate(),
                fontSize = 30.sp,
                color = CoreColors.background.textColor,
                fontFamily = PlayfairDisplay
            )
        }
    }
}
