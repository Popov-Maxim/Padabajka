package com.padabajka.dating.feature.subscription.data.billing

import android.content.Context
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
import com.padabajka.dating.core.platform.data.activity.AndroidActivityProvider
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidBillingClient(
    context: Context,
    private val activityProvider: AndroidActivityProvider,
    private val billingProductMapper: BillingProductMapper
) : PlatformBillingClient {

    private val billingClient = BillingClient.newBuilder(context.applicationContext)
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

    // TODO(P2): Correlate late callbacks from cancelled purchases with their originating flow.
    @Volatile
    private var pendingPurchase: CompletableDeferred<PurchaseResult?>? = null

    override val serviceName: PlatformBillingClient.ServiceName
        get() = PlatformBillingClient.ServiceName.GOOGLE_PLAY

    override suspend fun getSubscriptions(): List<SubscriptionProduct> {
        return querySubscriptions()
    }

    override suspend fun launchPurchase(offerId: String): PurchaseResult? =
        withContext(Dispatchers.Main.immediate) {
            val product = queryProduct(PRODUCT_ID) ?: return@withContext null

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

            val activity = activityProvider.currentActivity
                ?: error("Cannot launch a purchase without a resumed Activity")

            check(pendingPurchase == null) { "A purchase is already in progress" }
            val purchaseResult = CompletableDeferred<PurchaseResult?>()
            pendingPurchase = purchaseResult

            try {
                billingClient.launchBillingFlow(activity, params)
                purchaseResult.await()
            } finally {
                pendingPurchase = null
                purchaseResult.cancel()
            }
        }

    private fun handlePurchaseUpdates(
        result: BillingResult,
        purchases: List<Purchase>?
    ) {
        if (result.responseCode != BillingClient.BillingResponseCode.OK || purchases == null) {
            return
        }
        val purchaseResult = pendingPurchase

        purchases.forEach { purchase ->

            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                val productId = purchase.products.firstOrNull() ?: return

                if (productId == PRODUCT_ID) {
                    purchaseResult?.complete(
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

        val connectionResult = CompletableDeferred<Boolean>()
        this.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                connectionResult.complete(result.responseCode == BillingClient.BillingResponseCode.OK)
            }

            override fun onBillingServiceDisconnected() {
                connectionResult.complete(false)
            }
        })
        return connectionResult.await()
    }

    companion object {
        private const val PRODUCT_ID = "premium_monthly"
    }
}
