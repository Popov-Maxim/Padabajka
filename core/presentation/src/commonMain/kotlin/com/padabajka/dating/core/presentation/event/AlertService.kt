package com.padabajka.dating.core.presentation.event

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AlertService {
    private val _alerts = MutableSharedFlow<Alert>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val alerts = _alerts.asSharedFlow()

    suspend fun showAlert(alert: Alert) {
        _alerts.emit(alert)
    }

    suspend fun showAlert(text: @Composable () -> String) {
        showAlert(Alert(text))
    }
}

data class Alert(val text: @Composable () -> String)
