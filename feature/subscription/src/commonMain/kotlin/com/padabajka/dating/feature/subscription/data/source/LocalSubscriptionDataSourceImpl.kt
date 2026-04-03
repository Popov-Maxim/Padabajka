package com.padabajka.dating.feature.subscription.data.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.feature.subscription.data.source.model.SubscriptionStateDto
import kotlinx.coroutines.flow.Flow

class LocalSubscriptionDataSourceImpl(
    private val datastore: DataStore<SubscriptionStateDto>
) : LocalSubscriptionDataSource {
    override val subscriptionState: Flow<SubscriptionStateDto> = datastore.data

    override suspend fun update(update: (SubscriptionStateDto) -> SubscriptionStateDto) {
        datastore.updateData(update)
    }
}
