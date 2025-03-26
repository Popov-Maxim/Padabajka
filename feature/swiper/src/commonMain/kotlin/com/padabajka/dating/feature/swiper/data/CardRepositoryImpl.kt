package com.padabajka.dating.feature.swiper.data

import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.NativeAdRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.swiper.AdCard
import com.padabajka.dating.core.repository.api.model.swiper.Card
import com.padabajka.dating.core.repository.api.model.swiper.EmptyCard
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonCard
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.core.repository.api.model.swiper.Reaction
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

class CardRepositoryImpl(
    private val personRepository: PersonRepository,
    private val nativeAdRepository: NativeAdRepository,
    private val reactionRepository: ReactionRepository,
    private val cardSelectorProvider: CardSelectorProvider
) : CardRepository {

    override suspend fun getNextCard(searchPreferences: SearchPreferences): Card {
        val cardSelector = cardSelectorProvider.getCardSelector(searchPreferences)
        val type = cardSelector.nextType()
        val card = when (type) {
            CardSelector.Type.Ad -> getAdCard(searchPreferences)
            CardSelector.Type.Person -> getPersonCard(searchPreferences)
        }

        withCardType(card) { actualType ->
            cardSelector.add(actualType)
        }

        return card
    }

    override suspend fun react(reaction: Reaction) {
        if (reaction is PersonReaction) {
            personRepository.setUsed(reaction.id)
        }
        reactionRepository.react(reaction)
    }

    private suspend fun getAdCard(searchPreferences: SearchPreferences): Card {
        val nativeAd = nativeAdRepository.loadNextAd()
        return nativeAd?.toCard() ?: getPersonCard(searchPreferences)
    }

    private suspend fun getPersonCard(searchPreferences: SearchPreferences): Card {
        val person = personRepository.getPerson(searchPreferences)

        return person?.toCard() ?: EmptyCard
    }

    private fun Person.toCard(): PersonCard {
        return PersonCard(this)
    }

    private fun PlatformNativeAd.toCard(): AdCard {
        return AdCard(this)
    }

    private inline fun withCardType(card: Card, action: (CardSelector.Type) -> Unit) {
        when (card) {
            is AdCard -> action(CardSelector.Type.Ad)
            is PersonCard -> action(CardSelector.Type.Person)
            EmptyCard -> Unit
        }
    }
}
