package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language

class ChangeLanguageUseCase(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(language: Language.Static) {
        appSettingsRepository.updateAppSettings {
            copy(language = language)
        }
    }
}
