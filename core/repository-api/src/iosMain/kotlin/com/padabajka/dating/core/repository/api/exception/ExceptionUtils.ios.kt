package com.padabajka.dating.core.repository.api.exception

actual fun Throwable.isConnectException(): Boolean {
    return this.message?.contains("Failed to connect", ignoreCase = true) ?: false
}
