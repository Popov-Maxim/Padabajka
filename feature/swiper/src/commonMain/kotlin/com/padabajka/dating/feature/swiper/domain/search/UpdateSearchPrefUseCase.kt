package com.padabajka.dating.feature.swiper.domain.search

import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

class UpdateSearchPrefUseCase(
    private val searchPreferencesRepository: SearchPreferencesRepository
) {
    suspend operator fun invoke(update: (SearchPreferences) -> SearchPreferences) {
        searchPreferencesRepository.update(update)
    }
}
