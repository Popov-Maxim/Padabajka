package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class AgeRange(val start: Age, val endInclusive: Age)

@JvmInline
@Serializable
value class Age(val raw: Int) {
    init {
        require(raw in MIN_AGE_VALUE..MAX_AGE_VALUE) {
            "$raw years old is not included [$MIN_AGE_VALUE, $MAX_AGE_VALUE]"
        }
    }

    companion object {
        private const val MIN_AGE_VALUE = 18
        private const val MAX_AGE_VALUE = 80
        val minAge = Age(raw = MIN_AGE_VALUE)
        val maxAge = Age(raw = MAX_AGE_VALUE)
    }
}

fun ageOf(value: Int): Age = Age(value)

fun Int.toAge(): Age = Age(this)

operator fun Age.rangeTo(that: Age): AgeRange = AgeRange(this, that)
