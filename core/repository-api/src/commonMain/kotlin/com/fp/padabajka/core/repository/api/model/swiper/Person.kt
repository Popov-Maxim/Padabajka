package com.fp.padabajka.core.repository.api.model.swiper

import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class Person(
    val id: PersonId,
    val profile: Profile,
)

@JvmInline
@Serializable
value class PersonId(
    val raw: String
)
