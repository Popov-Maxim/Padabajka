package com.padabajka.dating.core.repository.api.model.profile

data class Detail(
    val type: String,
    val value: Value,
    val icon: ServerIcon?
) {
    sealed interface Value {
        data class String(val raw: kotlin.String) : Value
        data class Asset(val raw: Text) : Value
        data class Centimeter(val raw: Double) : Value
    }
}

object DetailType {
    const val City = "city"
    const val SexualOrientation = "sexual_orientation"
    const val Education = "education"
    const val Profession = "profession"
    const val Height = "height"
    const val Character = "character"
    const val Language = "language"
}
