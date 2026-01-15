package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AppSettingsComponentProvider(private val repository: AppSettingsRepository) {
    val languages: Flow<Language.Static>
        get() = repository.appSettings
            .map { it.language }
            .distinctUntilChanged()
}
