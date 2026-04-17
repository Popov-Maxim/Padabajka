package com.padabajka.dating.core.networking.config

import com.padabajka.dating.shared.firebase.perf.Firebase
import com.padabajka.dating.shared.firebase.perf.metrics.HttpMethod
import com.padabajka.dating.shared.firebase.perf.perf
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin

@Suppress("TooGenericExceptionCaught")
val FirebasePlugin = createClientPlugin("FirebasePlugin") {
    on(Send) { request ->
        if (
            request.url.protocol.name !in listOf("http", "https")
        ) {
            return@on proceed(request)
        }

        val httpMethod = HttpMethod.fromRaw(request.method.value)
        val metric = Firebase.perf
            .newHttpMetric(request.url.toString(), httpMethod)

        metric.start()

        try {
            val response = proceed(request)

            metric.setHttpResponseCode(response.response.status.value)
            metric.stop()

            response
        } catch (t: Throwable) {
            metric.stop()
            throw t
        }
    }
}
