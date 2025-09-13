package com.padabajka.dating.core.repository.api.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class DebugAppSettings(
    val host: Host = Host.Prod,
    val showFps: Boolean = false
)
