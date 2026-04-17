package com.padabajka.dating.shared.firebase.perf.metrics

enum class HttpMethod(val raw: String) {
    GET("GET"),
    PUT("PUT"),
    CONNECT("CONNECT"),
    PATCH("PATCH"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    DELETE("DELETE"),
    POST("POST"),
    HEAD("HEAD"),
    UNKNOWN("UNKNOWN");

    companion object {
        private val mapper = entries.associateBy(HttpMethod::raw)

        fun fromRaw(raw: String): HttpMethod = mapper[raw] ?: UNKNOWN
    }
}
