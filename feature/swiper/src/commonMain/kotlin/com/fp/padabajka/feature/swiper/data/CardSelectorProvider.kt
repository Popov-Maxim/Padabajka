package com.fp.padabajka.feature.swiper.data

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

class CardSelectorProvider(
    private val cardSelectorFactory: Factory<CardSelector>
) {

    private var previousSearchPreferences: SearchPreferences? = null

    private lateinit var cardSelector: CardSelector

    fun getCardSelector(searchPreferences: SearchPreferences): CardSelector {
        if (previousSearchPreferences != searchPreferences) {
            previousSearchPreferences = searchPreferences
            cardSelector = cardSelectorFactory.get()
        }

        return cardSelector
    }
}
