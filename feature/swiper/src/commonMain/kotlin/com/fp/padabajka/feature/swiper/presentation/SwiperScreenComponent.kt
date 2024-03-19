package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.Reaction
import com.fp.padabajka.feature.swiper.domain.ReactToPersonUseCase
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperState

class SwiperScreenComponent(
    context: ComponentContext,
    private val reactToPersonUseCase: Factory<ReactToPersonUseCase>,
) : BaseComponent<SwiperState>(
    context,
    TODO()
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
                reactToPersonUseCase.get().invoke(Reaction.Dislike(personId))
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
                reactToPersonUseCase.get().invoke(Reaction.Like(personId))
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
                reactToPersonUseCase.get().invoke(Reaction.SuperLike(personId))
            },
            mapper = {
                it // TODO
            },
            update = { swiperState, _ ->
                swiperState
            }
        )
}
