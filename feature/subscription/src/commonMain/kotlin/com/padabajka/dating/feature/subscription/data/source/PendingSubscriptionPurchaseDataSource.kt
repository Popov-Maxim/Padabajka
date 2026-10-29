package com.padabajka.dating.feature.subscription.data.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.feature.subscription.data.source.model.PendingSubscriptionPurchaseDto
import com.padabajka.dating.feature.subscription.data.source.model.PendingSubscriptionPurchasesDto

class PendingSubscriptionPurchaseDataSource(
    private val datastore: DataStore<PendingSubscriptionPurchasesDto>
) {
    suspend fun save(purchase: PendingSubscriptionPurchaseDto) {
        datastore.updateData { pending ->
            pending.copy(purchases = (pending.purchases + purchase).distinct())
        }
    }

    suspend fun remove(purchase: PendingSubscriptionPurchaseDto) {
        datastore.updateData { pending ->
            pending.copy(purchases = pending.purchases.filterNot { it == purchase })
        }
    }
}
