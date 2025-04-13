package com.padabajka.dating.core.repository.api.model.messenger

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class ChatId(val raw: String)
