package com.fp.padabajka.feature.auth.data.model

data class UserDto(
    val id: String,
    val email: String?,
    val isEmailVerified: Boolean
)
