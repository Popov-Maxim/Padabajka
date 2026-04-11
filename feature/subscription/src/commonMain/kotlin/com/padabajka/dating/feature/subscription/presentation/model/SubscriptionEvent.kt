package com.padabajka.dating.feature.subscription.presentation.model

sealed interface SubscriptionEvent {
    data object Apply : SubscriptionEvent
    data object NavigateBack : SubscriptionEvent
}
