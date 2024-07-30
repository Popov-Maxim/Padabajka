package com.fp.padabajka.core.repository.api.model.profile

import kotlin.jvm.JvmInline

data class AgeRange(val start: Age, val endInclusive: Age)

@JvmInline
value class Age(val raw: Int)

val minAge = Age(raw = 18)
val maxAge = Age(raw = 80)

fun ageOf(value: Int): Age = Age(value)

fun Int.toAge(): Age = Age(this)

operator fun Age.rangeTo(that: Age): AgeRange = AgeRange(this, that)
