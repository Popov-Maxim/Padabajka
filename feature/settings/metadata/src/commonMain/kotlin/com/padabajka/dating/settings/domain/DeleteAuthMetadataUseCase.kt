package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.metadata.MetadataRepository

class DeleteAuthMetadataUseCase(
    private val metadataRepository: MetadataRepository
) {
    suspend operator fun invoke() {
        val deviceUid = metadataRepository.getDeviceUid()

        metadataRepository.deleteAuthMetadata(deviceUid)
    }
}
