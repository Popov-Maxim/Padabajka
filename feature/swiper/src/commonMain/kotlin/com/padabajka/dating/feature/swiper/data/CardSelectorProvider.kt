package com.padabajka.dating.feature.swiper.data

import com.padabajka.dating.core.data.MutableAtomic
import com.padabajka.dating.core.data.mutableAtomic
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

class CardSelectorProvider(
    private val cardSelectorFactory: Factory<CardSelector>
) {

    private var previousSearchPreferences: MutableAtomic<SearchPreferences?> = mutableAtomic(null)

    private lateinit var cardSelector: CardSelector

    suspend fun getCardSelector(searchPreferences: SearchPreferences): CardSelector {
        previousSearchPreferences.update {
            if (this != searchPreferences) {
                cardSelector = cardSelectorFactory.get()
            }

            searchPreferences
        }

        return cardSelector
    }
}
