package com.padabajka.dating.feature.swiper.domain

import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.updateFeatures

class ReturnLastCardUseCase(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke() {
        subscriptionRepository.update { state ->
            state.updateFeatures {
                it.copy(returns = it.returns - 1)
            }
        }
    }
}
