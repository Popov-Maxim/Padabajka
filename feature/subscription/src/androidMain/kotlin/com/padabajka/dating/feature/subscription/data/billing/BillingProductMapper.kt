package com.padabajka.dating.feature.subscription.data.billing

import com.android.billingclient.api.ProductDetails
import com.padabajka.dating.core.repository.api.model.subscription.Price
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionDiscount
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionOffer
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPeriod
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import kotlin.math.roundToInt

class BillingProductMapper {

    fun map(productDetails: ProductDetails): SubscriptionProduct {
        return SubscriptionProduct(
            id = productDetails.productId,
            title = productDetails.name,
            description = productDetails.description,
            offers = productDetails.subscriptionOfferDetails
                ?.map { it.toDomainOffer() }
                ?: emptyList()
        )
    }

    private fun ProductDetails.SubscriptionOfferDetails.toDomainOffer(): SubscriptionOffer {
        val phases = pricingPhases.pricingPhaseList

        val firstPhase = phases.firstOrNull()

        return SubscriptionOffer(
            id = offerToken,
            period = SubscriptionPeriod.mapPeriod(firstPhase?.billingPeriod),
            price = firstPhase?.toPrice() ?: Price(
                amount = "",
                currencyCode = "",
                formatted = ""
            ),
            discount = calculateDiscount(phases),
            isTrial = phases.any { it.priceAmountMicros == 0L },
            isRecommended = false
        )
    }

    private fun ProductDetails.PricingPhase.toPrice(): Price {
        return Price(
            amount = priceAmountMicros.toString(),
            currencyCode = priceCurrencyCode,
            formatted = formattedPrice
        )
    }

    private fun calculateDiscount(
        phases: List<ProductDetails.PricingPhase>
    ): SubscriptionDiscount? {
        val paidPhases = phases.filter { it.priceAmountMicros > 0 }

        if (paidPhases.size < 2) return null

        val original = paidPhases.first()
        val final = paidPhases.last()

        val originalPrice = original.priceAmountMicros.toDouble()
        val finalPrice = final.priceAmountMicros.toDouble()

        return if (originalPrice == 0.0) {
            null
        } else {
            val percent = (((finalPrice - originalPrice) / finalPrice) * FULL_PERCENT).roundToInt()

            SubscriptionDiscount(
                percent = percent,
                originalPrice = final.toPrice()
            )
        }
    }

    companion object {
        private const val FULL_PERCENT = 100
    }
}
