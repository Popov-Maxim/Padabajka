package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.model.subscription.Price
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionDiscount
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionOffer
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPeriod
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.currencyCode
import platform.StoreKit.SKProduct
import platform.StoreKit.SKProductDiscount
import platform.StoreKit.SKProductDiscountPaymentMode
import platform.StoreKit.SKProductPeriodUnit
import platform.StoreKit.SKProductSubscriptionPeriod
import kotlin.math.roundToInt

class SKProductMapper {

    fun map(product: SKProduct): SubscriptionProduct {
        return SubscriptionProduct(
            id = product.productIdentifier,
            title = product.localizedTitle,
            description = product.localizedDescription,
            offers = buildOffers(product)
        )
    }

    private fun buildOffers(product: SKProduct): List<SubscriptionOffer> {
        val baseOffer = SubscriptionOffer(
            id = product.productIdentifier,
            period = product.subscriptionPeriod?.toDomain() ?: SubscriptionPeriod.Month,
            price = product.toPrice(),
            discount = null,
            isTrial = false,
            isRecommended = false
        )

        val discountOffers = product.discounts
            .mapNotNull { it as? SKProductDiscount }
            .map { discount ->

                SubscriptionOffer(
                    id = "${product.productIdentifier}_${discount.identifier ?: "discount"}",
                    period = product.subscriptionPeriod?.toDomain() ?: SubscriptionPeriod.Month,
                    price = discount.toPrice(product),
                    discount = discount.toDomainDiscount(product),
                    isTrial = discount.paymentMode ==
                        SKProductDiscountPaymentMode.SKProductDiscountPaymentModeFreeTrial,
                    isRecommended = false
                )
            }

        return listOf(baseOffer) + discountOffers
    }

    private fun SKProduct.toPrice(): Price {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            locale = priceLocale
        }

        return Price(
            amount = price.stringValue,
            currencyCode = priceLocale.currencyCode ?: "",
            formatted = formatter.stringFromNumber(price) ?: ""
        )
    }

    private fun SKProductDiscount.toPrice(product: SKProduct): Price {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            locale = product.priceLocale
        }

        return Price(
            amount = price.stringValue,
            currencyCode = product.priceLocale.currencyCode ?: "",
            formatted = formatter.stringFromNumber(price) ?: ""
        )
    }

    @Suppress("ReturnCount")
    private fun SKProductDiscount.toDomainDiscount(product: SKProduct): SubscriptionDiscount? {
        val original = product.toPrice()
        val discounted = toPrice(product)

        val originalAmount = original.amount.toDoubleOrNull() ?: return null
        val discountedAmount = discounted.amount.toDoubleOrNull() ?: return null

        val percent = ((1 - (discountedAmount / originalAmount)) * FULL_PERCENT).roundToInt()

        return SubscriptionDiscount(
            percent = percent,
            originalPrice = original
        )
    }

    private fun SKProductSubscriptionPeriod.toDomain(): SubscriptionPeriod? {
        return when (unit) {
            SKProductPeriodUnit.SKProductPeriodUnitWeek -> SubscriptionPeriod.Week
            SKProductPeriodUnit.SKProductPeriodUnitMonth -> SubscriptionPeriod.Month
            SKProductPeriodUnit.SKProductPeriodUnitYear -> SubscriptionPeriod.Year
            else -> SubscriptionPeriod.Month
        }
    }

    companion object {
        private const val FULL_PERCENT = 100
    }
}
