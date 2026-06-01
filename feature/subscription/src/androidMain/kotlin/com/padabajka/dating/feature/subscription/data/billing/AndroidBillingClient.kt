package com.padabajka.dating.feature.subscription.data.billing

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidBillingClient(
    private val context: Activity,
    private val billingProductMapper: BillingProductMapper
) : PlatformBillingClient {

    private val billingClient = BillingClient.newBuilder(context)
        .setListener { billingResult, purchases ->
            handlePurchaseUpdates(billingResult, purchases)
        }
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .enablePrepaidPlans()
                .build()
        )
        .build()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val purchaseResultFlow = MutableSharedFlow<PurchaseResult>() // TODO(P0)
    override val serviceName: PlatformBillingClient.ServiceName
        get() = PlatformBillingClient.ServiceName.GOOGLE_PLAY

    override suspend fun getSubscriptions(): List<SubscriptionProduct> {
        return querySubscriptions()
    }

    override suspend fun launchPurchase(offerId: String): PurchaseResult? {
        val product = queryProduct(PRODUCT_ID) ?: return null

        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(product)
                        .setOfferToken(offerId)
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(context, params)

        return purchaseResultFlow.first { it.productId == PRODUCT_ID }
    }

    private fun handlePurchaseUpdates(
        result: BillingResult,
        purchases: List<Purchase>?
    ) {
        if (result.responseCode != BillingClient.BillingResponseCode.OK || purchases == null) {
            return
        }

        purchases.forEach { purchase ->

            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                val productId = purchase.products.firstOrNull() ?: return

                scope.launch {
                    purchaseResultFlow.emit(
                        PurchaseResult(
                            productId = productId,
                            purchaseToken = purchase.purchaseToken
                        )
                    )
                }

                if (!purchase.isAcknowledged) {
                    val ackParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(ackParams) {}
                }
            }
        }
    }

    private suspend fun querySubscriptions(): List<SubscriptionProduct> {
        if (billingClient.startConnectionWait().not()) return listOf()

        val query = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        val products = billingClient.queryProductDetails(query)
        val mapped = products.productDetailsList?.map {
            billingProductMapper.map(it)
        } ?: listOf()

        return mapped
    }

    private suspend fun queryProduct(productId: String): ProductDetails? {
        val query = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        val products = billingClient.queryProductDetails(query)
        return products.productDetailsList?.first()
    }

    private suspend fun BillingClient.startConnectionWait(): Boolean {
        if (this.isReady) return true

        return suspendCancellableCoroutine { continuation ->
            this.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    continuation.resume(false)
                }
            })
        }
    }

    companion object {
        private const val PRODUCT_ID = "premium_monthly"
    }
}
