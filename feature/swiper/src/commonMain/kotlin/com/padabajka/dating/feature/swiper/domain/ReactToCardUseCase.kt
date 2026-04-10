package com.padabajka.dating.feature.swiper.domain

import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.core.repository.api.model.swiper.Reaction

class ReactToCardUseCase(
    private val cardRepository: CardRepository,
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(reaction: Reaction) {
        if (reaction is PersonReaction.SuperLike) {
            subscriptionRepository.update { usage ->
                usage.copy(
                    superLikes = usage.superLikes + 1
                )
            }
        }
        cardRepository.react(reaction)
    }
}
