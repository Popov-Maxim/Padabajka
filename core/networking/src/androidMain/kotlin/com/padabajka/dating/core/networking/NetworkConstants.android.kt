package com.padabajka.dating.core.networking

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual val localHost: String
    get() = "10.0.2.2"
actual val networkEngineFactory: HttpClientEngineFactory<*>
    get() = CIO
