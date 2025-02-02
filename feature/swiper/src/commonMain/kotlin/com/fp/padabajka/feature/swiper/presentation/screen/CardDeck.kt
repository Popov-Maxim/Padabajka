package com.fp.padabajka.feature.swiper.presentation.screen

import com.fp.padabajka.core.domain.indexOf
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

class CardDeck(
    private val mainCollection: PersistentList<CardItem> = persistentListOf(),
    private val indexForDelete: Int = 0
) {
    fun add(cardItem: CardItem): CardDeck {
        val newMainCollection = mainCollection.add(cardItem)

        return CardDeck(newMainCollection, indexForDelete)
    }

    fun addAll(cardItems: List<CardItem>): CardDeck {
        val newMainCollection = mainCollection.addAll(cardItems)

        return CardDeck(newMainCollection, indexForDelete)
    }

    fun remove(cardItem: CardItem): CardDeck {
        val index = mainCollection.indexOf(cardItem, indexForDelete)

        return if (index == indexForDelete) {
            CardDeck(mainCollection, indexForDelete + 1)
        } else {
            CardDeck(mainCollection.remove(cardItem), indexForDelete)
        }
    }

    fun removeOldCards(): CardDeck {
        return CardDeck(mainCollection.drop(indexForDelete).toPersistentList())
    }

    fun getCards(): ImmutableCollection<CardItem> {
        return mainCollection
    }
}
