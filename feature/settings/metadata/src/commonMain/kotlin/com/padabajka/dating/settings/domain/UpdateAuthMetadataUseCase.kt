package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository

class UpdateAuthMetadataUseCase(
    private val metadataRepository: MetadataRepository,
) {
    suspend operator fun invoke(
        updateMetadata: (AuthMetadata) -> AuthMetadata
    ) {
        val deviceUid = metadataRepository.getDeviceUid()
        val emptyMetadata = AuthMetadata(deviceUid, null)
        val newMetadata = updateMetadata(emptyMetadata)

        metadataRepository.updateAuthMetadata(newMetadata)
    }
}
