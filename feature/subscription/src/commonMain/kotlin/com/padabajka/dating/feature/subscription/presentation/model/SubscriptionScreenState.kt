package com.padabajka.dating.feature.subscription.presentation.model

import com.padabajka.dating.core.presentation.State

data class SubscriptionScreenState(
    val monthSub: SubscriptionInfo
) : State

data class SubscriptionInfo(
    val oldPrice: String,
    val discount: Int,
    val price: String
)
