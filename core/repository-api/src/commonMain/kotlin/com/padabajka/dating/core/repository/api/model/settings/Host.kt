package com.padabajka.dating.core.repository.api.model.settings

import kotlinx.serialization.Serializable

@Serializable
sealed interface Host {
    @Serializable
    data object Prod : Host

    @Serializable
    data class Local(val host: String, val port: Int? = null) : Host

    @Serializable
    data class Custom(val host: String, val port: Int? = null) : Host
}

fun Host.raw(): String? {
    return when (this) {
        is Host.Custom -> host
        is Host.Local -> host
        Host.Prod -> null
    }
}

fun Host.rawPort(): Int? {
    return when (this) {
        is Host.Custom -> port
        is Host.Local -> port
        Host.Prod -> null
    }
}

fun Host.isHttps(): Boolean {
    return when (this) {
        is Host.Custom -> false
        is Host.Local -> false
        Host.Prod -> true
    }
}
