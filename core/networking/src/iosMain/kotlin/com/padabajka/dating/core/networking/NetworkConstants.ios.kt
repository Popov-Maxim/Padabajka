package com.padabajka.dating.core.networking

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual val localHost: String
    get() = "localhost"
actual val imageEngine: HttpClientEngine
    get() = Darwin.create()
