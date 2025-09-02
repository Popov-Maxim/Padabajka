package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.datetime.LocalDate

data class DraftProfile(
    val name: String? = null,
    val birthday: LocalDate? = null,
    val gender: Gender? = null,
    val mainImage: Image? = null,
    val images: List<Image> = listOf(),
    val aboutMe: String? = null,
    val lookingFor: LookingForData? = null,
    val details: List<Detail> = listOf(),
    val lifestyles: List<Lifestyle> = listOf()
)
