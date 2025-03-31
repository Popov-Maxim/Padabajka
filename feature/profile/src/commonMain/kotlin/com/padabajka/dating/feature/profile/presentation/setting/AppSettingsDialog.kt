package com.padabajka.dating.feature.profile.presentation.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.padabajka.dating.core.domain.IpAddressProvider
import com.padabajka.dating.core.domain.MutableAppSettings
import com.padabajka.dating.core.networking.localHost
import org.koin.compose.koinInject

@Composable
fun AppSettingsDialog(
    settings: MutableAppSettings = koinInject(),
    ipAddressProvider: IpAddressProvider = koinInject(),
    onDismiss: () -> Unit
) {
    var ipAddress by remember { mutableStateOf(ipAddressProvider.getIpAddress() ?: "") }
    var selectedHost by remember { mutableStateOf(SelectedHost.Default) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // IP Address Setting
                TextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text(text = "IP Address") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedHost == SelectedHost.Custom
                )

                Row {
                    SelectedHost.entries.forEach { host ->
                        val isSelected = host == selectedHost
                        val backgroundColor =
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                        val textColor = if (isSelected) Color.White else Color.Unspecified
                        TextButton(
                            modifier = Modifier.background(backgroundColor),
                            onClick = { selectedHost = host },
                        ) {
                            Text(
                                text = host.name,
                                color = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            updateIpAddress(settings, selectedHost, ipAddress)
                            onDismiss()
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

private enum class SelectedHost {
    Default,
    Localhost,
    Custom
}

private fun updateIpAddress(
    settings: MutableAppSettings,
    selectedHost: SelectedHost,
    ipAddress: String
) {
    settings.host = when (selectedHost) {
        SelectedHost.Default -> null
        SelectedHost.Localhost -> localHost
        SelectedHost.Custom -> ipAddress
    }
}
