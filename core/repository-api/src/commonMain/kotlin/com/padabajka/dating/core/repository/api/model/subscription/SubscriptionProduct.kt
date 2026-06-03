package com.padabajka.dating.core.repository.api.model.subscription

data class SubscriptionProduct(
    val id: String,
    val title: String,
    val description: String?,
    val offers: List<SubscriptionOffer>,
)

data class SubscriptionOffer(
    val id: String,
    val period: SubscriptionPeriod,
    val price: Price,
    val discount: SubscriptionDiscount?,
    val isTrial: Boolean,
    val isRecommended: Boolean,
)
data class Price(
    val amount: String,
    val currencyCode: String,
    val formatted: String,
)
sealed interface SubscriptionPeriod {
    data object Week : SubscriptionPeriod
    data object Month : SubscriptionPeriod
    data object Year : SubscriptionPeriod

    companion object {
        fun mapPeriod(period: String?): SubscriptionPeriod {
            return when (period) {
                "P1W" -> Week
                "P1M" -> Month
                "P1Y" -> Year
                else -> Month
            }
        }
    }
}

data class SubscriptionDiscount(
    val percent: Int,
    val originalPrice: Price,
)
