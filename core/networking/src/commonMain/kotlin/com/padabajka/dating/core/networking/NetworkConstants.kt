package com.padabajka.dating.core.networking

import com.padabajka.dating.core.repository.api.model.settings.Host
import com.padabajka.dating.core.repository.api.model.settings.isHttps
import com.padabajka.dating.core.utils.isDebugBuild
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.http.DEFAULT_PORT
import io.ktor.http.URLProtocol

object NetworkConstants {
    val protocol = URLProtocol.HTTPS
    val domainName = if (isDebugBuild) "dev.padabajka.com" else "padabajka.com"
    const val PORT = DEFAULT_PORT
    const val LOCAL_PORT = 5858

    val path = "${protocol.name}://$domainName"
}

fun Host.protocol() = if (isHttps().not()) URLProtocol.HTTP else null
fun Host.socketProtocol() = if (isHttps().not()) URLProtocol.WS else null

expect val localHost: String

expect val networkEngineFactory: HttpClientEngineFactory<*>
