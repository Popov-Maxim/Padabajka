package com.padabajka.dating.feature.subscription.data.source

import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionStateResponse
import com.padabajka.dating.feature.subscription.data.network.SubscriptionApi
import com.padabajka.dating.feature.subscription.data.source.model.PurchaseRequest

class RemoteSubscriptionDataSource(
    private val api: SubscriptionApi
) {
    suspend fun loadSubscriptionState(): SubscriptionStateResponse {
        return api.getState()
    }

    suspend fun subscribe(request: PurchaseRequest): SubscriptionStateResponse {
        return api.purchase(request)
    }
}
