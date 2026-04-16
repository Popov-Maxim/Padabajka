package com.padabajka.dating.core.repository.api.model.common

import kotlinx.serialization.Serializable

@Serializable
data class CoreRect(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)
