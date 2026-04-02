package com.padabajka.dating.feature.subscription.presentation.model

import com.padabajka.dating.core.presentation.State

data class SubscriptionScreenState(
    val monthSub: SubscriptionInfo
) : State

data class SubscriptionInfo(
    val price: String
)
