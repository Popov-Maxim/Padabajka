package com.padabajka.dating.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.sync.SyncSessionObserver
import org.koin.compose.koinInject

@Composable
fun SyncStatusBanner(modifier: Modifier = Modifier) {
    val settings: AppSettingsRepository = koinInject()
    val debugAppSettings by settings.debugAppSettings.collectAsState()
    if (debugAppSettings.showSyncStatus.not()) return

    val syncSessionObserver: SyncSessionObserver = koinInject()
    val state by syncSessionObserver.stateFlow.collectAsState()

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.Gray)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = "SyncManager: $state",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
