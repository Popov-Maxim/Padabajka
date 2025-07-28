package com.padabajka.dating.core.repository.api.model.profile

import kotlin.jvm.JvmInline

data class Text(
    val id: Id,
    val type: Type,
    val default: String? = null
) {
    @JvmInline
    value class Id(val raw: String)

    enum class Type(val raw: String) {
        UI("ui"),
        City("city"),
        Default("default");

        companion object {
            private val mapper = entries.associateBy { it.raw }

            fun parse(raw: String): Type = mapper[raw] ?: Default
        }
    }
}
