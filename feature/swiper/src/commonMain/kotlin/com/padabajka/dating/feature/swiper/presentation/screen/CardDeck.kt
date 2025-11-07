package com.padabajka.dating.feature.swiper.presentation.screen

import com.padabajka.dating.core.domain.indexOf
import com.padabajka.dating.core.presentation.event.StateEvent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.event.raised
import com.padabajka.dating.feature.swiper.presentation.model.CardItem
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.max

data class CardDeck(
    private val mainCollection: PersistentList<CardItem> = persistentListOf(),
    private val indexForDelete: Int = 0,
    val returnLastEvent: StateEvent = consumed
) {

    val indexFirstCard = indexForDelete

    fun add(cardItem: CardItem): CardDeck {
        val newMainCollection = mainCollection.add(cardItem)

        return CardDeck(newMainCollection, indexForDelete)
    }

    fun addAll(cardItems: List<CardItem>): CardDeck {
        val newMainCollection = mainCollection.addAll(cardItems)

        return CardDeck(newMainCollection, indexForDelete)
    }

    fun remove(cardItem: CardItem): CardDeck {
        val saveRemoveCards = true
        val index = mainCollection.indexOf(cardItem, indexForDelete)

        return if (index == indexForDelete || saveRemoveCards) {
            CardDeck(mainCollection, indexForDelete + 1)
        } else {
            CardDeck(mainCollection.remove(cardItem), indexForDelete)
        }
    }

    fun returnLast(): CardDeck {
        val indexForDelete = max(indexForDelete - 1, 0)
        return CardDeck(mainCollection, indexForDelete, raised)
    }

    fun makeStatic(): CardDeck {
        return copy(returnLastEvent = consumed)
    }

    fun removeOldCards(): CardDeck {
        return CardDeck(mainCollection.drop(indexForDelete).toPersistentList())
    }

    fun getCards(): ImmutableCollection<CardItem> {
        return mainCollection
    }
}
