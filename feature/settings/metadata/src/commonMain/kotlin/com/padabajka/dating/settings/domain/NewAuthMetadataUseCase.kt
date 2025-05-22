package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.core.repository.api.metadata.PushRepository

class NewAuthMetadataUseCase(
    private val metadataRepository: MetadataRepository,
    private val pushRepository: PushRepository
) {
    suspend operator fun invoke() {
        val deviceUid = metadataRepository.getDeviceUid()
        val token = pushRepository.getToken()
        val authMetadata = AuthMetadata(deviceUid, token)

        metadataRepository.updateAuthMetadata(authMetadata)
    }
}
