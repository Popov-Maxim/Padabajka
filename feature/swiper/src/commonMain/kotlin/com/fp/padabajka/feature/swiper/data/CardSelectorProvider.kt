package com.fp.padabajka.feature.swiper.data

import com.fp.padabajka.core.data.MutableAtomic
import com.fp.padabajka.core.data.mutableAtomic
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

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
