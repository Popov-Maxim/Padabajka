package com.fp.padabajka.core.repository.api.model.profile

import kotlinx.datetime.LocalDate

data class Profile(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val birthday: LocalDate,
    val images: List<Image>,
    val aboutMe: String,
    val details: List<Detail>,
    val achievements: List<Achievement>
)
