package com.padabajka.dating.feature.reaction.screen.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.reaction.screen.domain.ReactionsToMeUseCase
import com.padabajka.dating.feature.reaction.screen.domain.sortByPriorityType
import com.padabajka.dating.feature.reaction.screen.presentation.model.LikesMeEvent
import com.padabajka.dating.feature.reaction.screen.presentation.model.LikesMeState
import com.padabajka.dating.feature.reaction.screen.presentation.model.ListReactions
import com.padabajka.dating.feature.reaction.screen.presentation.model.toUIState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class LikesMeScreenComponent(
    context: ComponentContext,
    private val reactionsToMeUseCase: ReactionsToMeUseCase,
    private val reactionRepository: ReactionRepository,
) : BaseComponent<LikesMeState>(
    context,
    LikesMeState(listReactions = ListReactions.Idle)
) {

    init {
        componentScope.launch {
            val reactionUIState = reactionsToMeUseCase().sortByPriorityType().map { reaction ->
                reaction.toUIState()
            }
            reduce { state ->
                state.copy(
                    listReactions = ListReactions.Success(
                        likes = reactionUIState.toPersistentList()
                    )
                )
            }
        }
    }

    fun onEvent(event: LikesMeEvent) {
        when (event) {
            is LikesMeEvent.Dislike -> dislike(event.personId)
            is LikesMeEvent.Like -> like(event.personId)
        }
    }

    private fun dislike(personId: PersonId) {
        val reaction = PersonReaction.Dislike(personId)
        reactPersonAndUpdateCardDeck(reaction)
    }

    private fun like(personId: PersonId) {
        val reaction = PersonReaction.Like(personId)
        reactPersonAndUpdateCardDeck(reaction)
    }

    private fun reactPersonAndUpdateCardDeck(reaction: PersonReaction) =
        mapAndReduceException(
            action = {
                reactionRepository.react(reaction) // TODO()
            },
            mapper = {
                it // TODO()
            },
            update = { swiperState, exception ->
                if (exception == null) {
                    val list = (swiperState.listReactions as? ListReactions.Success)?.likes
                        ?: return@mapAndReduceException swiperState
                    val newList = list.removeAll { it.personId == reaction.id }

                    swiperState.copy(listReactions = ListReactions.Success(newList))
                } else {
                    swiperState // TODO()
                }
            }
        )
}
