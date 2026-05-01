package com.padabajka.dating.core.repository.api.exception

import java.net.ConnectException

actual fun Throwable.isConnectException(): Boolean {
    return this is ConnectException
}
