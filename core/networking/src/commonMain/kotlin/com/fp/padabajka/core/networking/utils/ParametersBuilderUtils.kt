package com.fp.padabajka.core.networking.utils

import io.ktor.http.ParametersBuilder

fun ParametersBuilder.appendNotNull(name: String, value: String?) {
    if (value != null) {
        append(name, value)
    }
}
