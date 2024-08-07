package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.delegate
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.domain.NextCardUseCase
import com.fp.padabajka.feature.swiper.domain.ReactToCardUseCase
import com.fp.padabajka.feature.swiper.domain.search.SearchPreferencesProvider
import com.fp.padabajka.feature.swiper.domain.search.UpdateSearchPrefUseCase
import com.fp.padabajka.feature.swiper.presentation.model.ApplySearchPrefEvent
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.PersonItem
import com.fp.padabajka.feature.swiper.presentation.model.SearchPreferencesItem
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperState
import com.fp.padabajka.feature.swiper.presentation.model.toSearchPreferences
import com.fp.padabajka.feature.swiper.presentation.model.toUICardItem
import com.fp.padabajka.feature.swiper.presentation.model.toUISearchPreferences
import com.fp.padabajka.feature.swiper.presentation.screen.CardDeck
import kotlinx.coroutines.launch

class SwiperScreenComponent(
    context: ComponentContext,
    reactToCardUseCaseFactory: Factory<ReactToCardUseCase>,
    nextCardUseCaseFactory: Factory<NextCardUseCase>,
    updateSearchPrefUseCaseFactory: Factory<UpdateSearchPrefUseCase>,
    searchPreferencesProvider: SearchPreferencesProvider
) : BaseComponent<SwiperState>(
    context,
    SwiperState(
        CardDeck(),
        SearchPreferencesItem.Loading
    )
) {

    init {
        componentScope.launch {
            searchPreferencesProvider.get().collect { searchPreferences ->
                reduce {
                    it.copy(
                        cardDeck = CardDeck(),
                        searchPreferences = searchPreferences.toUISearchPreferences()
                    )
                }
                updateCardDeck(count = 3)
            }
        }
    }

    private val reactToCardUseCase: ReactToCardUseCase by reactToCardUseCaseFactory.delegate()
    private val nextCardUseCase: NextCardUseCase by nextCardUseCaseFactory.delegate()
    private val updateSearchPrefUseCase: UpdateSearchPrefUseCase by
        updateSearchPrefUseCaseFactory.delegate()

    fun onEvent(event: SwiperEvent) {
        when (event) {
            is DislikeEvent -> dislikeCard(event.cardItem)
            is LikeEvent -> likeCard(event.cardItem)
            is SuperLikeEvent -> superLikeCard(event.cardItem)
            is EndOfCardAnimationEvent -> removeCardFromDeck(event.cardItem)
            is ApplySearchPrefEvent -> applySearchPref(event.searchPreferencesItem)
        }
    }

    private fun applySearchPref(searchPreferencesItem: SearchPreferencesItem) {
        val searchPreferences = (searchPreferencesItem as? SearchPreferencesItem.Success)
            ?.toSearchPreferences() ?: return
        updateSearchPref(searchPreferences)
    }

    private fun updateSearchPref(searchPreferences: SearchPreferences) = mapAndReduceException(
        action = {
            updateSearchPrefUseCase.invoke { searchPreferences }
        },
        mapper = {
            it // TODO
        },
        update = { swiperState, _ ->
            swiperState
        }
    )

    private fun removeCardFromDeck(cardItem: CardItem) {
        reduce { state ->
            state.run { copy(cardDeck = cardDeck.remove(cardItem)) }
        }
    }

    private fun dislikeCard(cardItem: CardItem) {
        if (cardItem is PersonItem) {
            reactPersonAndUpdateCardDeck(PersonReaction.Dislike(cardItem.id))
        } else {
            updateCardDeck()
        }
    }

    private fun likeCard(cardItem: CardItem) {
        if (cardItem is PersonItem) {
            reactPersonAndUpdateCardDeck(PersonReaction.Dislike(cardItem.id))
        } else {
            updateCardDeck()
        }
    }

    private fun superLikeCard(cardItem: CardItem) {
        if (cardItem is PersonItem) {
            reactPersonAndUpdateCardDeck(PersonReaction.Dislike(cardItem.id))
        } else {
            updateCardDeck()
        }
    }

    private fun reactPersonAndUpdateCardDeck(reaction: PersonReaction) =
        mapAndReduceException(
            action = {
                reactToCardUseCase(reaction)
                updateCardDeck()
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )

    private fun updateCardDeck(count: Int = 1) {
        mapAndReduceException(
            action = {
                val cards = nextCardUseCase(count)
                val uiCards = cards.map { it.toUICardItem() }
                reduce { state ->
                    state.run { copy(cardDeck = cardDeck.addAll(uiCards)) }
                }
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )
    }
}
