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
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.padabajka.dating.core.platform.data.activity.AndroidActivityProvider
import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.core.repository.api.exception.SubscriptionPurchaseException
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
                .build()
        )
        .build()

    // TODO(P2): Correlate late callbacks from cancelled purchases with their originating flow.
    @Volatile
    private var pendingPurchase: CompletableDeferred<PurchaseResult>? = null

    override val serviceName: PlatformBillingClient.ServiceName
        get() = PlatformBillingClient.ServiceName.GOOGLE_PLAY

    override suspend fun getSubscriptions(): List<SubscriptionProduct> {
        return querySubscriptions()
    }

    override suspend fun launchPurchase(offerId: String, userId: String): PurchaseResult =
        withContext(Dispatchers.Main.immediate) {
            val product = queryProduct(PRODUCT_ID) ?: throw SubscriptionException.ProductUnavailable()

            val params = BillingFlowParams.newBuilder()
                .setObfuscatedAccountId(userId)
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
                ?: throw SubscriptionPurchaseException()

            check(pendingPurchase == null) { "A purchase is already in progress" }
            val purchaseResult = CompletableDeferred<PurchaseResult>()
            pendingPurchase = purchaseResult

            try {
                val launchResult = billingClient.launchBillingFlow(activity, params)
                when (launchResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> purchaseResult.await()
                    BillingClient.BillingResponseCode.USER_CANCELED -> PurchaseResult.Cancelled
                    else -> throw launchResult.toSubscriptionException()
                }
            } finally {
                pendingPurchase = null
                purchaseResult.cancel()
            }
        }

    override suspend fun restorePurchase(): PurchaseResult.Purchased? =
        withContext(Dispatchers.Main.immediate) {
            if (!billingClient.startConnectionWait()) throw SubscriptionException.StoreUnavailable()

            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
            val result = billingClient.queryPurchasesAsync(params)
            if (result.billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                throw result.billingResult.toSubscriptionException()
            }

            val purchase = result.purchasesList.firstOrNull { PRODUCT_ID in it.products }
                ?: return@withContext null
            if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
                throw SubscriptionException.RecoveryFailed()
            }
            val token = purchase.purchaseToken.takeIf { it.isNotBlank() }
                ?: throw SubscriptionException.RecoveryFailed()

            PurchaseResult.Purchased(PRODUCT_ID, token)
        }

    private fun handlePurchaseUpdates(
        result: BillingResult,
        purchases: List<Purchase>?
    ) {
        val purchaseResult = pendingPurchase
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> handleSuccessfulPurchaseUpdate(purchases, purchaseResult)
            BillingClient.BillingResponseCode.USER_CANCELED -> purchaseResult?.complete(PurchaseResult.Cancelled)
            else ->
                purchaseResult?.completeExceptionally(result.toSubscriptionException())
        }
    }

    private fun handleSuccessfulPurchaseUpdate(
        purchases: List<Purchase>?,
        purchaseResult: CompletableDeferred<PurchaseResult>?
    ) {
        val purchase = purchases?.firstOrNull { PRODUCT_ID in it.products }
        if (purchase == null) {
            purchaseResult?.completeExceptionally(SubscriptionException.RecoveryFailed())
            return
        }

        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                val token = purchase.purchaseToken.takeIf { it.isNotBlank() }
                if (token == null) {
                    purchaseResult?.completeExceptionally(SubscriptionException.RecoveryFailed())
                    return
                }
                purchaseResult?.complete(PurchaseResult.Purchased(PRODUCT_ID, token))

                if (!purchase.isAcknowledged) {
                    val ackParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(ackParams) {}
                }
            }
            else -> purchaseResult?.completeExceptionally(SubscriptionPurchaseException())
        }
    }

    private fun BillingResult.toSubscriptionException(): SubscriptionException = when (responseCode) {
        BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> SubscriptionException.PaymentNotAllowed()
        BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
        BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> SubscriptionException.ProductUnavailable()
        BillingClient.BillingResponseCode.DEVELOPER_ERROR -> SubscriptionException.ConfigurationError()
        BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> SubscriptionException.RecoveryFailed()
        BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> SubscriptionException.PurchaseNotFound()
        BillingClient.BillingResponseCode.NETWORK_ERROR,
        BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
        BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> SubscriptionException.StoreUnavailable()
        else -> SubscriptionPurchaseException()
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
        if (products.billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            throw products.billingResult.toSubscriptionException()
        }
        return products.productDetailsList?.firstOrNull()
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
