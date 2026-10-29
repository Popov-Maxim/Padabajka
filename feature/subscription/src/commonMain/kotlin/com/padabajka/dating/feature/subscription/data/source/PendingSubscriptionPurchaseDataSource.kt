package com.padabajka.dating.feature.subscription.data.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.feature.subscription.data.source.model.PendingSubscriptionPurchaseDto
import com.padabajka.dating.feature.subscription.data.source.model.PendingSubscriptionPurchasesDto
import kotlinx.coroutines.flow.first

class PendingSubscriptionPurchaseDataSource(
    private val datastore: DataStore<PendingSubscriptionPurchasesDto>
) {
    suspend fun get(userId: String, offerId: String): PendingSubscriptionPurchaseDto? {
        return datastore.data.first().purchases.firstOrNull {
            it.userId == userId && it.offerId == offerId
        }
    }

    suspend fun save(purchase: PendingSubscriptionPurchaseDto) {
        datastore.updateData { pending ->
            pending.copy(purchases = (pending.purchases + purchase).distinct())
        }
    }

    suspend fun removeAll(userId: String) {
        datastore.updateData { pending ->
            pending.copy(purchases = pending.purchases.filterNot { it.userId == userId })
        }
    }
}
