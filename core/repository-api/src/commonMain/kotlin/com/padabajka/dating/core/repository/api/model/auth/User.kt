package com.padabajka.dating.core.repository.api.model.auth

import kotlin.jvm.JvmInline

data class User(
    val id: UserId,
    val email: String
)

@JvmInline
value class UserId(val raw: String)
