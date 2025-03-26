package com.fp.padabajka.core.networking

import io.ktor.client.engine.HttpClientEngine

object NetworkConstants {
    val domainName = localHost
    const val PORT = 5858
}

expect val localHost: String

expect val imageEngine: HttpClientEngine
