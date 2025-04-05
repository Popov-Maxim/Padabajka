package com.padabajka.dating.core.repository.api.model.profile

import kotlin.jvm.JvmInline

data class Text(
    val id: Id,
    val default: String? = null
) {
    @JvmInline
    value class Id(val raw: String)
}
