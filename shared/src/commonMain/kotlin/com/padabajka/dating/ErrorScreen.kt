package com.padabajka.dating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.settings.presentation.setting.AppSettingsDialog

@Composable
fun ErrorScreen(
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp, alignment = Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null
        )
        Text(
            text = message,
            fontSize = 20.sp
        )

        SettingButton()
    }
}

@Composable
private fun SettingButton(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showDialog = true
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "settings"
        )
    }

    if (showDialog) {
        AppSettingsDialog { showDialog = false }
    }
}
