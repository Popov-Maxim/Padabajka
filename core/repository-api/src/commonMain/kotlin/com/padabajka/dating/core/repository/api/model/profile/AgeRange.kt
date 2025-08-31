package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class AgeRange(val start: Age, val endInclusive: Age)

@JvmInline
@Serializable
value class Age(val raw: Int) {

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
operator fun AgeRange.contains(age: Age): Boolean {
    return age.raw in this.start.raw..this.endInclusive.raw
}

operator fun Age.compareTo(other: Age): Int = this.raw.compareTo(other.raw)
