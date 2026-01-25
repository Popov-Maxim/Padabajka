package com.padabajka.dating.feature.profile.data.asset.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetsDto<T>(
    val assets: List<T>,
    val version: Int
)
