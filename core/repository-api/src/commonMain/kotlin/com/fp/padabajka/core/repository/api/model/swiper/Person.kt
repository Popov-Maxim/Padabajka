package com.fp.padabajka.core.repository.api.model.swiper

import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlin.jvm.JvmInline

data class Person(
    val id: PersonId,
    val profile: Profile,
)

@JvmInline
value class PersonId(
    val raw: String
)
