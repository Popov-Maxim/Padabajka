package com.padabajka.dating.feature.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthPreferences(
    val email: String? = null
)
