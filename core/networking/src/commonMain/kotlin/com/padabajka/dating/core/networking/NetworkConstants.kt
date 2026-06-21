package com.padabajka.dating.core.networking

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.http.DEFAULT_PORT
import io.ktor.http.URLProtocol

object NetworkConstants {
    val protocol = URLProtocol.HTTPS
    const val domainName = "padabajka.com"
    const val PORT = DEFAULT_PORT
    const val LOCAL_PORT = 5858

    val path = "${protocol.name}://$domainName"
}

expect val localHost: String

expect val networkEngineFactory: HttpClientEngineFactory<*>
