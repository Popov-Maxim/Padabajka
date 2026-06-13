package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.core.repository.api.metadata.PushRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone

class NewAuthMetadataUseCase(
    private val metadataRepository: MetadataRepository,
    private val pushRepository: PushRepository,
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke() {
        val deviceUid = metadataRepository.getDeviceUid()
        val token = pushRepository.getToken()
        val language = appSettingsRepository.appSettings.first().language
        val lang = language.id
        val timeZone = TimeZone.currentSystemDefault().id

        val authMetadata = AuthMetadata(deviceUid, token, lang, timeZone)

        runCatching {
            metadataRepository.updateAuthMetadata(authMetadata)
        }.onFailure {
            println("TODO: not impl for error NewAuthMetadataUseCase")
        }
    }
}
