package com.padabajka.dating.core.repository.api.metadata

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class AuthMetadata(
    val deviceUid: DeviceUid,
    val notificationToken: String?,
)

@Serializable
@JvmInline
value class DeviceUid(val raw: String)
