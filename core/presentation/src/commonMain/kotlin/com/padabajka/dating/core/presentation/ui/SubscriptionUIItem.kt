package com.padabajka.dating.core.presentation.ui

import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState

data class SubscriptionUIItem(
    val isActive: Boolean,
    val showLikes: Boolean,
    val superLikes: Int,
    val returns: Int
)

fun SubscriptionState.toUI(): SubscriptionUIItem {
    return SubscriptionUIItem(
        isActive = isActive,
        showLikes = features.showLikes,
        superLikes = features.superLikes,
        returns = features.returns
    )
}
