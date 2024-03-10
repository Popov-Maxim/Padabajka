package com.fp.padabajka.core.repository.api.model.profile

import kotlinx.datetime.LocalDate
import kotlin.jvm.JvmInline

data class Profile(
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val images: List<Image>,
    val aboutMe: String,
    val details: List<Detail>,
    val mainAchievement: Achievement?,
    val achievements: List<Achievement>
)

@JvmInline
value class Id(
    val raw: Int
)

data class Person(
    val id: Id,
    val profile: Profile,
)
