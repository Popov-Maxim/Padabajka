package com.padabajka.dating.core.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AppSettings {
    val host: String?
    val showFps: StateFlow<Boolean>
}

class MutableAppSettings : AppSettings {

    var hostData: Host = Host.Prod

    override val host: String?
        get() {
            return when (val host = hostData) {
                is Host.Custom -> host.host
                is Host.Local -> host.host
                Host.Prod -> null
            }
        }
    override var showFps: MutableStateFlow<Boolean> = MutableStateFlow(false)
}

sealed interface Host {
    data object Prod : Host
    data class Local(val host: String) : Host
    data class Custom(val host: String) : Host
}
