package com.fp.padabajka.core.networking

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual val localHost: String
    get() = "10.0.2.2"
actual val imageEngine: HttpClientEngine
    get() = CIO.create()
