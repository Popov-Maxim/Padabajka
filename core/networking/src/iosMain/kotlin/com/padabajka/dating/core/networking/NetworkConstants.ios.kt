package com.padabajka.dating.core.networking

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual val localHost: String
    get() = "localhost"
actual val networkEngineFactory: HttpClientEngineFactory<*>
    get() = Darwin
