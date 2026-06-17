package com.padabajka.dating.feature.subscription.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPeriod
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionEvent
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionInfo
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionScreenState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class SubscriptionScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val subscriptionRepository: SubscriptionRepository,
    private val alertService: AlertService,
) : BaseComponent<SubscriptionScreenState>(
    context,
    "subscription",
    initState(subscriptionRepository)
) {

    init {
        componentScope.launch {
            val subscriptionProduct =
                subscriptionRepository.subscriptionProducts.mapNotNull { products ->
                    products.find { it.offers.firstOrNull()?.period == SubscriptionPeriod.Month }
                }
            combine(
                subscriptionProduct,
                subscriptionRepository.subscriptionState
            ) { product, state ->
                product to state
            }.collect { (product, state) ->
                reduce {
                    if (state.isActive) {
                        SubscriptionScreenState.HasSubscription
                    } else {
                        product.toUI()
                    }
                }
            }
        }
    }

    fun onEvent(event: SubscriptionEvent) {
        when (event) {
            SubscriptionEvent.Apply -> subscription()
            SubscriptionEvent.NavigateBack -> navigateBack()
        }
    }

    private fun subscription() {
        val subState = state.value as? SubscriptionScreenState.Success ?: return
        launchStep(
            action = {
                subscriptionRepository.subscribe(subState.monthSub.offerId)
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

    private companion object {

        private fun initState(subscriptionRepository: SubscriptionRepository): SubscriptionScreenState {
            return if (subscriptionRepository.subscriptionStateValue.isActive) {
                SubscriptionScreenState.HasSubscription
            } else {
                val product = subscriptionRepository.subscriptionProductsValue.find {
                    it.offers.firstOrNull()?.period == SubscriptionPeriod.Month
                }

                product?.toUI() ?: SubscriptionScreenState.Loading
            }
        }

        private fun SubscriptionProduct.toUI(): SubscriptionScreenState.Success {
            val offer = offers.first()
            return SubscriptionScreenState.Success(
                monthSub = SubscriptionInfo(
                    offerId = offer.id,
                    oldPrice = offer.discount?.originalPrice?.formatted ?: "",
                    discount = offer.discount?.percent ?: 0,
                    price = offer.price.formatted
                )
            )
        }
    }
}
