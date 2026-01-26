package com.padabajka.dating.core.repository.api.model.profile

enum class Gender(val raw: String) {
    MALE("male"),
    FEMALE("female"),
    Everyone("everyone");

    companion object {
        private val mapper = Gender.entries.associateBy { it.raw }

        fun parse(raw: String): Gender = mapper[raw]!!
    }
}
