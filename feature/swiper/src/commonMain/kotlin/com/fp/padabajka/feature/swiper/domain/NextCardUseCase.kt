package com.fp.padabajka.feature.swiper.domain

import com.fp.padabajka.core.repository.api.CardRepository
import com.fp.padabajka.core.repository.api.model.swiper.Card
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

class NextCardUseCase(private val cardRepository: CardRepository) {

    suspend operator fun invoke(searchPreferences: SearchPreferences): Card {
        return cardRepository.getNextCard(searchPreferences)
    }
}
