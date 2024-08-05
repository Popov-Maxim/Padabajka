package com.fp.padabajka.feature.swiper.presentation.screen

import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet

class CardDeck(
    private val mainCollection: PersistentSet<CardItem> = persistentSetOf(),
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
        val index = mainCollection.indexOf(cardItem)

        return if (index == 0) {
            CardDeck(mainCollection, indexForDelete + 1)
        } else {
            CardDeck(mainCollection.remove(cardItem), indexForDelete)
        }
    }

    fun removeOldCards(): CardDeck {
        return CardDeck(mainCollection.drop(indexForDelete).toPersistentSet())
    }

    fun getCards(): ImmutableCollection<CardItem> {
        return mainCollection
    }
}
