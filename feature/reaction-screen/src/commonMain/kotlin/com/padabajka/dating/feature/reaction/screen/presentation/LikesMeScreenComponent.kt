package com.padabajka.dating.feature.reaction.screen.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.toUI
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
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
    private val openSubscriptionScreen: () -> Unit,
    private val reactionsToMeUseCase: ReactionsToMeUseCase,
    private val reactionRepository: ReactionRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val alertService: AlertService,
) : BaseComponent<LikesMeState>(
    context,
    "likes_me",
    LikesMeState(
        listReactions = ListReactions.Idle,
        subscriptionFeature = subscriptionRepository.subscriptionStateValue.toUI()
    )
) {

    init {
        componentScope.launch {
            reactionsToMeUseCase.reactionsToMe.collect {
                val reactionUIState = it.sortByPriorityType().map { reaction ->
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
        componentScope.launch {
            subscriptionRepository.subscriptionState.collect { newState ->
                reduce {
                    it.copy(
                        subscriptionFeature = newState.toUI()
                    )
                }
            }
        }
    }

    fun onEvent(event: LikesMeEvent) {
        when (event) {
            is LikesMeEvent.Dislike -> dislike(event.personId)
            is LikesMeEvent.Like -> like(event.personId)
            LikesMeEvent.OpenSubscription -> openSubscriptionScreen()
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
        launchStep(
            action = {
                reactionRepository.react(reaction) // TODO(P0): dont ignore exception
                reduce { swiperState ->
                    val list = (swiperState.listReactions as? ListReactions.Success)?.likes
                        ?: return@reduce swiperState
                    val newList = list.removeAll { it.personId == reaction.id }

                    swiperState.copy(listReactions = ListReactions.Success(newList))
                }
            },
            onError = {
                val error = when (it) {
                    is ExternalDomainError.TextError -> it
                    is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
                }

                alertService.showAlert { error.text.translate() }
                error.needLog.not()
            }
        )
}
