package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class Text(
    val id: Id,
    val type: Type,
    val default: String? = null
) {
    @JvmInline
    @Serializable
    value class Id(val raw: String)

    @Serializable
    enum class Type(val raw: String) {
        UI("ui"),
        City("city"),
        Language("language"),
        Default("default");

        companion object {
            private val mapper = entries.associateBy { it.raw }

            fun parse(raw: String): Type = mapper[raw] ?: Default
        }
    }
}
