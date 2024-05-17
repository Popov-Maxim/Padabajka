package com.fp.padabajka.feature.swiper.presentation.screen

import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

class CardDeck(
    private val mainCollection: PersistentSet<CardItem> = persistentSetOf(),
    private val listForRemove: PersistentList<CardItem> = persistentListOf()
) {
    fun add(cardItem: CardItem): CardDeck {
        val newMainCollection = mainCollection.add(cardItem)

        return CardDeck(newMainCollection, listForRemove)
    }

    fun remove(cardItem: CardItem): CardDeck {
        val index = mainCollection.indexOf(cardItem)

        val newListForRemove = listForRemove
            .takeIf { index == 0 }?.add(cardItem)
            ?: listForRemove

        val newMainCollection = mainCollection.remove(cardItem)

        return CardDeck(newMainCollection, newListForRemove)
    }

    fun getCards(): ImmutableCollection<CardItem> {
        return listForRemove.addAll(mainCollection)
    }
}
