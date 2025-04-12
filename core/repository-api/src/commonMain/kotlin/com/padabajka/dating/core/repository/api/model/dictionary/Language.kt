package com.padabajka.dating.core.repository.api.model.dictionary

sealed interface Language {
    val id: String

    enum class Static(override val id: String) : Language {
        EN(id = "en"),
        RU(id = "ru")
    }

    data class Dynamic(override val id: String) : Language
}
