package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.model.swiper.EmptyCard
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.domain.NextCardUseCase
import com.fp.padabajka.feature.swiper.domain.ReactToCardUseCase
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperState
import com.fp.padabajka.feature.swiper.presentation.model.toUICardItem
import com.fp.padabajka.feature.swiper.presentation.screen.toSafe
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SwiperScreenComponent(
    context: ComponentContext,
    private val reactToCardUseCase: Factory<ReactToCardUseCase>,
    private val nextCardUseCase: Factory<NextCardUseCase>,
) : BaseComponent<SwiperState>(
    context,
    SwiperState(
        persistentListOf<CardItem>().toSafe()
    )
) {

    init {
        componentScope.launch {
            loadCard(count = 3)
        }
    }

    private val searchPreferences: SearchPreferences = object : SearchPreferences {}

    fun onEvent(event: SwiperEvent) {
        when (event) {
            is DislikeEvent -> dislikePerson(event.personId)
            is LikeEvent -> likePerson(event.personId)
            is SuperLikeEvent -> superLikePerson(event.personId)
            is EndOfCardAnimationEvent -> removeCardFromDeck(event.cardItem)
        }
    }

    private fun removeCardFromDeck(cardItem: CardItem) {
        reduce { state ->
            state.run { copy(cardDeck = cardDeck.remove(cardItem)) }
        }
    }

    private suspend fun loadCard(count: Int = 1): Job {
        return componentScope.launch {
            repeat(count) {
                val card = nextCardUseCase.get().invoke(searchPreferences)
                reduce { state ->
                    val needAddInCardDeck = card !is EmptyCard || state.cardDeck.isEmpty()
                    return@reduce if (needAddInCardDeck) {
                        state.run { copy(cardDeck = cardDeck.add(card.toUICardItem())) }
                    } else {
                        state
                    }
                }
            }
        }
    }

    private fun dislikePerson(personId: PersonId) {
        reactPerson(PersonReaction.Dislike(personId))
    }

    private fun likePerson(personId: PersonId) {
        reactPerson(PersonReaction.Like(personId))
    }

    private fun superLikePerson(personId: PersonId) {
        reactPerson(PersonReaction.SuperLike(personId))
    }

    private fun reactPerson(reaction: PersonReaction) =
        mapAndReduceException(
            action = {
                reactToCardUseCase.get().invoke(reaction)
                loadCard()
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )
}
