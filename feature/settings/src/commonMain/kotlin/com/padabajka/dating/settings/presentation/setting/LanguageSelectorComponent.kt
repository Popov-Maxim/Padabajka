package com.padabajka.dating.settings.presentation.setting

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.settings.domain.AppSettingsComponentProvider
import com.padabajka.dating.settings.domain.ChangeLanguageUseCase
import com.padabajka.dating.settings.presentation.model.language.LanguageEvent
import com.padabajka.dating.settings.presentation.model.language.LanguageState

class LanguageSelectorComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val changeLanguageUseCase: ChangeLanguageUseCase,
    private val appSettingsComponentProvider: AppSettingsComponentProvider
) : BaseComponent<LanguageState>(
    context,
    LanguageState(Language.Static.EN)
) {

    init {
        mapAndReduceException(
            action = {
                appSettingsComponentProvider.languages
                    .collect { language ->
                        reduce {
                            it.copy(selectedLanguage = language)
                        }
                    }
            },
            mapper = { it },
            update = { state, m -> state }
        )
    }

    fun onEvent(event: LanguageEvent) {
        when (event) {
            is LanguageEvent.ChangeLanguage -> changeLanguage(event.language)
            LanguageEvent.NavigateBack -> navigateBack()
        }
    }

    private fun changeLanguage(language: Language.Static) = mapAndReduceException(
        action = {
            changeLanguageUseCase(language)
        },
        mapper = { it },
        update = { state, _ -> state }
    )
}
