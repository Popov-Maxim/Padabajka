package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import kotlinx.coroutines.flow.firstOrNull

class IosPurchaseRestorer(
    private val storeKit: StoreKit2Client
) {
    suspend fun restorePurchase(productId: String): PurchaseResult.Purchased? {
        val result = storeKit.currentEntitlements().firstOrNull { it.transaction.productId == productId }
            ?: return null
        val transaction = result.transaction

        if (result.verificationError != null || transaction.id.isBlank()) {
            throw SubscriptionException.RecoveryFailed()
        }
        return PurchaseResult.Purchased(transaction.productId, transaction.id)
    }
}
