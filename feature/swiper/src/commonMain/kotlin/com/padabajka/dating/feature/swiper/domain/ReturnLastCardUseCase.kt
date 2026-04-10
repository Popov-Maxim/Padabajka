package com.padabajka.dating.feature.swiper.domain

import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository

class ReturnLastCardUseCase(
    private val subscriptionRepository: SubscriptionRepository,
    private val reactionRepository: ReactionRepository
) {
    suspend operator fun invoke() {
        subscriptionRepository.update { usage ->
            usage.copy(
                returns = usage.returns + 1
            )
        }
        reactionRepository.deleteReaction()
    }
}
