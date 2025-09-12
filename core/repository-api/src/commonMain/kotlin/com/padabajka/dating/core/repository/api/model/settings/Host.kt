package com.padabajka.dating.core.repository.api.model.settings

import kotlinx.serialization.Serializable

@Serializable
sealed interface Host {
    @Serializable
    data object Prod : Host

    @Serializable
    data class Local(val host: String) : Host

    @Serializable
    data class Custom(val host: String) : Host
}

fun Host.raw(): String? {
    return when (this) {
        is Host.Custom -> host
        is Host.Local -> host
        Host.Prod -> null
    }
}
