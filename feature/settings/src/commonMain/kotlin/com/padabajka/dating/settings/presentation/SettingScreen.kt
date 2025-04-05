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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.settings.presentation.model.LogOutEvent
import com.padabajka.dating.settings.presentation.model.NavigateBackEvent
import com.padabajka.dating.settings.presentation.model.SettingsEvent
import com.padabajka.dating.settings.presentation.setting.AppSettingsDialog
import androidx.compose.ui.graphics.painter.Painter as ComposePainter

@Composable
fun SettingScreen(component: SettingScreenComponent) {
    var showDialog by remember { mutableStateOf(false) }

    CustomScaffold(
        topBar = { TopBar(component::onEvent) }
    ) {
        val settingsButtonData = listOf(
            SettingButtonData(
                iconData = CoreIcons.NavigationBar.Profile.toData(),
                text = "Name",
                secondText = null,
                onClick = {}
            ),
            SettingButtonData(
                iconData = Icons.Filled.Settings.toData(),
                text = "AppSettings",
                secondText = null,
                onClick = { showDialog = true }
            ),
            SettingButtonData(
                iconData = Icons.AutoMirrored.Filled.ExitToApp.toData(),
                text = "Logout",
                secondText = null,
                onClick = { component.onEvent(LogOutEvent) }
            )
        )
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 50.dp
            )
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "General",
                    fontSize = 24.sp
                )
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    settingsButtonData.onEach { data ->
                        SettingButton(data.iconData, data.text, data.secondText, data.onClick)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AppSettingsDialog { showDialog = false }
    }
}

private data class SettingButtonData(
    val iconData: IconData,
    val text: String,
    val secondText: String?,
    val onClick: () -> Unit
)

private sealed interface IconData {
    data class Painter(val painter: ComposePainter) : IconData
    data class Vector(val vector: ImageVector) : IconData
}

private fun ComposePainter.toData() = IconData.Painter(this)
private fun ImageVector.toData() = IconData.Vector(this)

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
                when (iconData) {
                    is IconData.Painter -> {
                        Icon(
                            painter = iconData.painter,
                            modifier = Modifier.size(30.dp),
                            contentDescription = null
                        )
                    }

                    is IconData.Vector -> {
                        Icon(
                            imageVector = iconData.vector,
                            modifier = Modifier.size(30.dp),
                            contentDescription = null
                        )
                    }
                }

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
                text = "Settings",
                fontSize = 30.sp,
                color = CoreColors.background.textColor,
                fontFamily = PlayfairDisplay
            )
        }
    }
}
