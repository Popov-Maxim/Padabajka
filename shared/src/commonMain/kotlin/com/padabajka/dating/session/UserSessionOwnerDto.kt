package com.padabajka.dating.session

import kotlinx.serialization.Serializable

@Serializable
data class UserSessionOwnerDto(
    val userId: String? = null
)
