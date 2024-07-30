package com.fp.padabajka.feature.swiper.domain.search

import com.fp.padabajka.core.repository.api.SearchPreferencesRepository
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

class UpdateSearchPrefUseCase(
    private val searchPreferencesRepository: SearchPreferencesRepository
) {
    suspend operator fun invoke(update: (SearchPreferences) -> SearchPreferences) {
        searchPreferencesRepository.update(update)
    }
}
