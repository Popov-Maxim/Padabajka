package com.padabajka.dating.settings.data.network

import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMetadataDto(
    val deviceUid: String,
    val notificationToken: String?,
)

@Serializable
data class DeleteMetadataDto(
    val deviceUid: String,
)

fun AuthMetadata.toDto(): UpdateMetadataDto {
    return UpdateMetadataDto(
        deviceUid.raw,
        notificationToken
    )
}
