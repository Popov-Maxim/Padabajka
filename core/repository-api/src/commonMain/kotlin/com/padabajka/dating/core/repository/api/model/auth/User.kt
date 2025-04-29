package com.padabajka.dating.core.repository.api.model.auth

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class User(
    val id: UserId,
    val email: String
)

@JvmInline
@Serializable
value class UserId(val raw: String)
