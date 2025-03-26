package com.padabajka.dating.feature.swiper.domain

import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.Card
import kotlinx.coroutines.flow.first

class NextCardUseCase(
    private val cardRepository: CardRepository,
    private val searchPreferencesRepository: SearchPreferencesRepository
) {

    suspend operator fun invoke(count: Int): List<Card> {
        val searchPreferences = searchPreferencesRepository.searchPreferences.first()
        return (1..count).mapNotNull { cardRepository.getNextCard(searchPreferences) }
    }
}
