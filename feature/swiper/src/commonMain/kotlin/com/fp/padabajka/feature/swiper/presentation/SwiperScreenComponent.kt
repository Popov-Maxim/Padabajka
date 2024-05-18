package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.delegate
import com.fp.padabajka.core.presentation.BaseComponent
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
import com.fp.padabajka.feature.swiper.presentation.screen.CardDeck
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SwiperScreenComponent(
    context: ComponentContext,
    reactToCardUseCaseFactory: Factory<ReactToCardUseCase>,
    nextCardUseCaseFactory: Factory<NextCardUseCase>,
) : BaseComponent<SwiperState>(
    context,
    SwiperState(
        CardDeck()
    )
) {

    init {
        componentScope.launch {
            loadCard(count = 3)
        }
    }

    private val reactToCardUseCase: ReactToCardUseCase by reactToCardUseCaseFactory.delegate()
    private val nextCardUseCase: NextCardUseCase by nextCardUseCaseFactory.delegate()

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

    private fun loadCard(count: Int = 1): Job {
        return componentScope.launch {
            repeat(count) {
                val card = nextCardUseCase(searchPreferences)
                reduce { state ->
                    state.run { copy(cardDeck = cardDeck.add(card.toUICardItem())) }
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
                reactToCardUseCase(reaction)
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
