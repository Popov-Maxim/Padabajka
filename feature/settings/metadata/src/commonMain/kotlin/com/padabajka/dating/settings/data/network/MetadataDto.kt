package com.padabajka.dating.settings.data.network

import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import com.padabajka.dating.core.repository.api.metadata.DeviceUid
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMetadataDto(
    val deviceUid: DeviceUid,
    val notificationToken: String?,
    val lang: String,
    val timeZone: String,
)

@Serializable
data class DeleteMetadataDto(
    val deviceUid: DeviceUid,
)

fun AuthMetadata.toDto(): UpdateMetadataDto {
    return UpdateMetadataDto(
        deviceUid,
        notificationToken,
        lang,
        timeZone
    )
}
