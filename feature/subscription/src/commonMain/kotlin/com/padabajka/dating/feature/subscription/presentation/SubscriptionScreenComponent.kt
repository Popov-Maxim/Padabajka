package com.padabajka.dating.feature.subscription.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionEvent
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionInfo
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionScreenState

class SubscriptionScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val subscriptionRepository: SubscriptionRepository
) : BaseComponent<SubscriptionScreenState>(
    context,
    SubscriptionScreenState(
        monthSub = SubscriptionInfo(
            oldPrice = "4.99 $",
            discount = 60,
            price = "1.99 $" // TODO: value for test
        )
    )
) {

    fun onEvent(event: SubscriptionEvent) {
        when (event) {
            SubscriptionEvent.Apply -> subscription()
            SubscriptionEvent.NavigateBack -> navigateBack()
        }
    }

    private fun subscription() {
        mapAndReduceException(
            action = {
                subscriptionRepository.subscribe()
                navigateBack()
            },
            mapper = { it },
            update = { state, _ ->
                state
            }
        )
    }
}
