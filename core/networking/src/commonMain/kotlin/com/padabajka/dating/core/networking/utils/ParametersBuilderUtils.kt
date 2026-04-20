package com.padabajka.dating.core.networking.utils

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.ParametersBuilder
import io.ktor.http.isSuccess

fun ParametersBuilder.appendNotNull(name: String, value: String?) {
    if (value != null) {
        append(name, value)
    }
}

fun HttpResponse.throwIfNotSuccessful() {
    if (!status.isSuccess()) {
        throw ResponseException(this, "HTTP error: $status")
    }
}

fun HttpResponse.takeIfHasContent(): HttpResponse? =
    takeIf { it.status != HttpStatusCode.NoContent }
