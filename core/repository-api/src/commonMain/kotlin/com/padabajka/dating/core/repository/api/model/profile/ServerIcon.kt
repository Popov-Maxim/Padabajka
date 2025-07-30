package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class ServerIcon(
    val url: String,
    val force: Boolean
)
