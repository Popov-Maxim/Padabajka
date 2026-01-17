package com.padabajka.dating.feature.profile.domain.asset

import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.model.profile.asset.City

class FindCitiesUseCase(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(query: String): List<City> {
        return repository.findCities(query)
    }
}
