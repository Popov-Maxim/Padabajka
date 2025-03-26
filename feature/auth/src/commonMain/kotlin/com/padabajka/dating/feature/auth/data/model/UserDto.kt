package com.padabajka.dating.feature.auth.data.model

internal data class UserDto(
    val id: String,
    val email: String?,
    val isEmailVerified: Boolean
)
