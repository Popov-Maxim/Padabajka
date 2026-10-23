package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.domain.usecase.UpdateAuthMetadataUseCase
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.core.repository.api.metadata.PushRepository
import com.padabajka.dating.core.utils.runCatchingCancellable
import com.padabajka.dating.shared.firebase.crashlytics.logFailureToCrashlytics
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone

class NewAuthMetadataUseCase(
    private val metadataRepository: MetadataRepository,
    private val pushRepository: PushRepository,
    private val appSettingsRepository: AppSettingsRepository
) : UpdateAuthMetadataUseCase {
    override suspend operator fun invoke() {
        val deviceUid = metadataRepository.getDeviceUid()
        val token = runCatchingCancellable {
            pushRepository.getToken()
        }.logFailureToCrashlytics().getOrNull()
        val language = appSettingsRepository.appSettings.first().language
        val lang = language.id
        val timeZone = TimeZone.currentSystemDefault().id

        val authMetadata = AuthMetadata(deviceUid, token, lang, timeZone)

        runCatchingCancellable {
            metadataRepository.updateAuthMetadata(authMetadata)
        }.logFailureToCrashlytics().getOrThrow()
    }
}
