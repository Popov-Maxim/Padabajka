package com.padabajka.dating.feature.subscription.presentation.model

import com.padabajka.dating.core.presentation.State

sealed interface SubscriptionScreenState : State {
    data object Loading : SubscriptionScreenState
    data object HasSubscription : SubscriptionScreenState
    data class Success(val monthSub: SubscriptionInfo) : SubscriptionScreenState
}

data class SubscriptionInfo(
    val offerId: String,
    val oldPrice: String,
    val discount: Int,
    val price: String
)
