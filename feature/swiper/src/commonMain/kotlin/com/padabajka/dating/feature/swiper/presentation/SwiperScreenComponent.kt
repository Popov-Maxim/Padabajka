package com.padabajka.dating.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.toUI
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.profile.ProfileState
import com.padabajka.dating.core.repository.api.model.swiper.EmptyCard
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.domain.NextCardUseCase
import com.padabajka.dating.feature.swiper.domain.ReactToCardUseCase
import com.padabajka.dating.feature.swiper.domain.ReturnLastCardUseCase
import com.padabajka.dating.feature.swiper.domain.search.SearchPreferencesProvider
import com.padabajka.dating.feature.swiper.domain.search.UpdateSearchPrefUseCase
import com.padabajka.dating.feature.swiper.presentation.model.ActionReturnEvent
import com.padabajka.dating.feature.swiper.presentation.model.ApplySearchPrefEvent
import com.padabajka.dating.feature.swiper.presentation.model.CardDeckState
import com.padabajka.dating.feature.swiper.presentation.model.CardItem
import com.padabajka.dating.feature.swiper.presentation.model.DislikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.padabajka.dating.feature.swiper.presentation.model.EndReturnLastCardEvent
import com.padabajka.dating.feature.swiper.presentation.model.LikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.OpenSubscriptionScreen
import com.padabajka.dating.feature.swiper.presentation.model.PersonItem
import com.padabajka.dating.feature.swiper.presentation.model.ResetSearchPrefEventToDefault
import com.padabajka.dating.feature.swiper.presentation.model.ReturnLastCardEvent
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesConstants
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesItem
import com.padabajka.dating.feature.swiper.presentation.model.SuperLikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.SwiperEvent
import com.padabajka.dating.feature.swiper.presentation.model.SwiperState
import com.padabajka.dating.feature.swiper.presentation.model.UnfreezeProfileEvent
import com.padabajka.dating.feature.swiper.presentation.model.toSearchPreferences
import com.padabajka.dating.feature.swiper.presentation.model.toUICardItem
import com.padabajka.dating.feature.swiper.presentation.model.toUISearchPreferences
import com.padabajka.dating.feature.swiper.presentation.screen.CardDeck
import com.padabajka.dating.feature.swiper.presentation.screen.hasDeleted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SwiperScreenComponent(
    context: ComponentContext,
    private val openSubscriptionScreen: () -> Unit,
    reactToCardUseCaseFactory: Factory<ReactToCardUseCase>,
    nextCardUseCaseFactory: Factory<NextCardUseCase>,
    private val updateSearchPrefUseCase: UpdateSearchPrefUseCase,
    searchPreferencesProvider: SearchPreferencesProvider,
    private val profileRepository: ProfileRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val returnLastCardUseCase: ReturnLastCardUseCase
) : BaseComponent<SwiperState>(
    context,
    "swiper",
    SwiperState(
        cardDeck = CardDeck(),
        cardDeckState = CardDeckState.Idle,
        searchPreferences = SearchPreferencesItem.Loading,
        subscriptionFeature = subscriptionRepository.subscriptionStateValue.toUI()
    )
) {

    init {
        componentScope.launch {
            combine(
                searchPreferencesProvider.get(),
                profileRepository.profileState,
            ) { searchPreferences, profileState ->
                searchPreferences to profileState
            }.collect { (searchPreferences, profileState) ->
                val criticalState = when (profileState) {
                    ProfileState.Idle -> null
                    is ProfileState.Existing -> {
                        if (profileState.profile.isFrozen) CardDeckState.Frozen else null
                    }

                    ProfileState.NotCreated -> null // CardDeckState.ProfileNotCreated
                }

                if (criticalState != null) {
                    reduce {
                        it.copy(
                            cardDeck = CardDeck(),
                            cardDeckState = criticalState,
                            searchPreferences = searchPreferences.toUISearchPreferences()
                        )
                    }
                } else {
                    reduce {
                        it.copy(
                            cardDeck = CardDeck(),
                            cardDeckState = CardDeckState.Loading,
                            searchPreferences = searchPreferences.toUISearchPreferences()
                        )
                    }
                    updateCardDeck(count = 3)
                }
            }
        }
        componentScope.launch {
            subscriptionRepository.subscriptionState.collect { newState ->
                reduce {
                    it.copy(subscriptionFeature = newState.toUI())
                }
            }
        }
    }

    private val reactToCardUseCase: ReactToCardUseCase by reactToCardUseCaseFactory.delegate()
    private val nextCardUseCase: NextCardUseCase by nextCardUseCaseFactory.delegate()

    fun onEvent(event: SwiperEvent) {
        when (event) {
            is DislikeEvent -> dislikeCard(event.cardItem)
            is LikeEvent -> likeCard(event.cardItem)
            is SuperLikeEvent -> superLikeCard(event.cardItem, event.message)
            is EndOfCardAnimationEvent -> removeCardFromDeck(event.cardItem)
            is ApplySearchPrefEvent -> applySearchPref(event.searchPreferencesItem)
            ReturnLastCardEvent -> reduceReturnLastCard()
            ActionReturnEvent -> returnLastCard()
            EndReturnLastCardEvent -> finishReturnLastCard()
            UnfreezeProfileEvent -> unfreezeProfile()
            ResetSearchPrefEventToDefault -> resetSearchPrefEventToDefault()
            OpenSubscriptionScreen -> openSubscriptionScreen()
        }
    }

    private fun applySearchPref(searchPreferencesItem: SearchPreferencesItem) {
        val searchPreferencesSuccess = (searchPreferencesItem as? SearchPreferencesItem.Success)
            ?: return
        applySearchPref(searchPreferencesSuccess)
    }

    private fun applySearchPref(searchPreferencesItem: SearchPreferencesItem.Success) {
        if (state.value.searchPreferences == searchPreferencesItem) return
        val searchPreferences = searchPreferencesItem.toSearchPreferences()

        updateSearchPref(searchPreferences)
    }

    private fun updateSearchPref(searchPreferences: SearchPreferences) = launchStep(
        action = {
            updateSearchPrefUseCase.invoke { searchPreferences }
        },
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
            reactPersonAndUpdateCardDeck(PersonReaction.Like(cardItem.id))
        } else {
            updateCardDeck()
        }
    }

    private fun superLikeCard(cardItem: CardItem, message: String) {
        if (cardItem is PersonItem) {
            if (subscriptionRepository.subscriptionStateValue.features.superLikes > 0) {
                reactPersonAndUpdateCardDeck(PersonReaction.SuperLike(cardItem.id, message))
            } else {
                reduceReturnLastCard()
                openSubscriptionScreen()
            }
        } else {
            updateCardDeck()
        }
    }

    private fun reactPersonAndUpdateCardDeck(reaction: PersonReaction) =
        launchStep(
            action = {
                reactToCardUseCase(reaction)
                updateCardDeck()
            },
        )

    private fun updateCardDeck(count: Int = 1) {
        launchStep(
            action = {
                val cards = nextCardUseCase(count)
                val uiCards = cards.filter { it !is EmptyCard }.map { it.toUICardItem() }
                reduce { state ->
                    state.run {
                        copy(
                            cardDeck = cardDeck.addAll(uiCards),
                            cardDeckState = if (uiCards.size != count) CardDeckState.Empty else cardDeckState
                        )
                    }
                }
            },
            onError = {
                val text = when (it) {
                    is ExternalDomainError.TextError -> {
                        if (it == ExternalDomainError.TextError.Internet) {
                            StaticTextId.UiId.InternetConnectionErrorDescription
                        } else {
                            StaticTextId.UiId.CardDeckErrorLoadingProfiles
                        }
                    }

                    is ExternalDomainError.Unknown -> {
                        StaticTextId.UiId.CardDeckErrorLoadingProfiles
                    }
                }
                reduce { state ->
                    state.copy(cardDeckState = CardDeckState.Error(text))
                }
                false
            },
        )
    }

    private fun returnLastCard() {
        if (subscriptionRepository.subscriptionStateValue.features.returns <= 0) {
            openSubscriptionScreen()
        } else if (state.value.cardDeck.hasDeleted()) {
            launchStep(
                action = {
                    reduceReturnLastCard()
                    returnLastCardUseCase()
                },
            )
        }
    }

    private fun reduceReturnLastCard() {
        reduce { state ->
            state.run { copy(cardDeck = cardDeck.returnLast()) }
        }
    }

    private fun unfreezeProfile() {
        launchStep(
            action = {
                profileRepository.setFreeze(false)
            },
        )
    }

    private fun resetSearchPrefEventToDefault() {
        val searchPreferencesItem = state.value.searchPreferences
        val searchPreferences = (searchPreferencesItem as? SearchPreferencesItem.Success)
            ?: return
        val defaultSearchPreferences = searchPreferences.copy(
            distanceInKm = SearchPreferencesConstants.MAX_DISTANCE,
            ageRange = SearchPreferencesConstants.maxAgeRange
        )
        applySearchPref(defaultSearchPreferences)
    }

    private fun finishReturnLastCard() {
        reduce { state ->
            state.run { copy(cardDeck = cardDeck.makeStatic()) }
        }
    }
}
