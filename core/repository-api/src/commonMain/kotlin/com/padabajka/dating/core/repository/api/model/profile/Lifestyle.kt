package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Lifestyle(
    val type: String,
    val value: Text?,
    val attributes: List<Text>
)

object LifestyleType {
    const val Smoking = "smoking"
    const val Alcohol = "alcohol"
    const val Animals = "animals"
}
