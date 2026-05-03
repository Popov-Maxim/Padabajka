package com.padabajka.dating.feature.subscription.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionEvent
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionInfo
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionScreenState

class SubscriptionScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val subscriptionRepository: SubscriptionRepository,
    private val alertService: AlertService
) : BaseComponent<SubscriptionScreenState>(
    context,
    "subscription",
    SubscriptionScreenState(
        monthSub = SubscriptionInfo(
            oldPrice = "4.99 $",
            discount = 60,
            price = "1.99 $" // TODO(P0): value for test
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
        launchStep(
            action = {
                subscriptionRepository.subscribe()
                navigateBack()
            },
            onError = {
                val error = when (it) {
                    is ExternalDomainError.TextError -> it
                    is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
                }

                alertService.showAlert { error.text.translate() }
                error.needLog.not()
            }
        )
    }
}
