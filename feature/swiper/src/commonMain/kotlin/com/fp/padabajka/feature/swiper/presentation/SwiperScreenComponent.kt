package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.feature.swiper.domain.ReactToCardUseCase
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperState

class SwiperScreenComponent(
    context: ComponentContext,
    private val reactToCardUseCase: Factory<ReactToCardUseCase>,
) : BaseComponent<SwiperState>(
    context,
    SwiperState.Default
) {
    fun onEvent(event: SwiperEvent) {
        when (event) {
            is DislikeEvent -> dislikePerson(event.personId)
            is LikeEvent -> likePerson(event.personId)
            is SuperLikeEvent -> superLikePerson(event.personId)
        }
    }

    private fun dislikePerson(personId: PersonId) =
        mapAndReduceException(
            action = {
                reactToCardUseCase.get().invoke(PersonReaction.Dislike(personId))
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )

    private fun likePerson(personId: PersonId) =
        mapAndReduceException(
            action = {
                reactToCardUseCase.get().invoke(PersonReaction.Like(personId))
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )

    private fun superLikePerson(personId: PersonId) =
        mapAndReduceException(
            action = {
                reactToCardUseCase.get().invoke(PersonReaction.SuperLike(personId))
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )
}
