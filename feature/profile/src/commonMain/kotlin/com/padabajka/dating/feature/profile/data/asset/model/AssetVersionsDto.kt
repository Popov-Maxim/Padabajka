package com.padabajka.dating.feature.profile.data.asset.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetVersionsDto(
    val cities: Int,
    val languages: Int,
    val interests: Int
) {
    enum class Type {
        Cities,
        Languages,
        Interests
    }

    companion object {
        fun default() = AssetVersionsDto(
            cities = 0,
            languages = 0,
            interests = 0
        )
    }
}
