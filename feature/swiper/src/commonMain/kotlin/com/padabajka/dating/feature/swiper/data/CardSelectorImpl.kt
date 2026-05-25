package com.padabajka.dating.feature.swiper.data

import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.utils.FeatureToggle

class CardSelectorImpl(
    private val subscriptionRepository: SubscriptionRepository
) : CardSelector {

    private var countTypesWithoutAd: Int = 0

    override fun nextType(): CardSelector.Type {
        val subPlan = subscriptionRepository.subscriptionStateValue
        val canShowAds = subPlan.features.hideAds.not()

        return if (canShowAds && countTypesWithoutAd >= AD_FREQUENCY || FeatureToggle.forceAd) {
            CardSelector.Type.Ad
        } else {
            CardSelector.Type.Person
        }
    }

    override fun add(type: CardSelector.Type) {
        countTypesWithoutAd = when (type) {
            CardSelector.Type.Ad -> 0
            CardSelector.Type.Person -> countTypesWithoutAd + 1
        }
    }

    companion object {
        private const val AD_FREQUENCY = 10
    }
}
