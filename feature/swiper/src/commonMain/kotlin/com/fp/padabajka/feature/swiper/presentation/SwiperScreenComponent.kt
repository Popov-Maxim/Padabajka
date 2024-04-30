package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.domain.NextCardUseCase
import com.fp.padabajka.feature.swiper.domain.ReactToCardUseCase
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.EmptyCardItem
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.LoadingItem
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperState
import com.fp.padabajka.feature.swiper.presentation.model.toUICardItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SwiperScreenComponent(
    context: ComponentContext,
    private val reactToCardUseCase: Factory<ReactToCardUseCase>,
    private val nextCardUseCase: Factory<NextCardUseCase>,
) : BaseComponent<SwiperState>(
    context,
    SwiperState(
        LoadingItem,
        LoadingItem
    )
) {

    init {
        componentScope.launch {
            repeat(2) {
                loadCard()
            }
        }
    }

    private val searchPreferences: SearchPreferences = object : SearchPreferences {}

    fun onEvent(event: SwiperEvent) {
        when (event) {
            is DislikeEvent -> dislikePerson(event.personId)
            is LikeEvent -> likePerson(event.personId)
            is SuperLikeEvent -> superLikePerson(event.personId)
        }
    }

    private fun updateForegroundCard() {
        reduce {
            it.copy(
                foregroundCardItem = it.backgroundCardItem,
                backgroundCardItem = LoadingItem
            )
        }
    }

    private suspend fun loadCard(): Job {
        return componentScope.launch {
            val card = nextCardUseCase.get().invoke(searchPreferences).toUICardItem()
            reduce { state ->
                if (state.foregroundCardItem.isEmpty()) {
                    SwiperState(card, LoadingItem)
                } else {
                    state.copy(backgroundCardItem = card)
                }
            }
        }
    }

    private fun CardItem.isEmpty(): Boolean {
        return this is LoadingItem || this is EmptyCardItem
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
                updateForegroundCard()
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
