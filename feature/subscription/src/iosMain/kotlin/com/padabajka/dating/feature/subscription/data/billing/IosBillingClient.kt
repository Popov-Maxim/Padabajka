package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.core.repository.api.exception.SubscriptionPurchaseException
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import com.padabajka.dating.core.utils.uuidFromKey
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSError
import platform.StoreKit.SKMutablePayment
import platform.StoreKit.SKPaymentQueue
import platform.StoreKit.SKPaymentTransaction
import platform.StoreKit.SKPaymentTransactionObserverProtocol
import platform.StoreKit.SKPaymentTransactionState
import platform.StoreKit.SKProduct
import platform.StoreKit.SKProductsRequest
import platform.StoreKit.SKProductsRequestDelegateProtocol
import platform.StoreKit.SKProductsResponse
import platform.StoreKit.SKRequest
import platform.darwin.NSObject

class IosBillingClient( // TODO(P0)
    private val skProductMapper: SKProductMapper,
    private val purchaseRestorer: IosPurchaseRestorer,
    private val storeKit: StoreKit2Client,
) : PlatformBillingClient {

    private var pendingPurchase: CompletableDeferred<PurchaseResult>? = null

    override val serviceName =
        PlatformBillingClient.ServiceName.APP_STORE

    override suspend fun getSubscriptions(): List<SubscriptionProduct> =
        withContext(Dispatchers.Main.immediate) {
            val request = SKProductsRequest(productIdentifiers = setOf(PRODUCT_ID))
            val delegate = ProductsDelegate()

            try {
                request.delegate = delegate
                request.start()
                delegate.result.await().map { skProductMapper.map(it) }
            } finally {
                request.delegate = null
                request.cancel()
                delegate.result.cancel()
            }
        }

    override suspend fun launchPurchase(offerId: String, userId: String): PurchaseResult =
        withContext(Dispatchers.Main.immediate) {
            if (!SKPaymentQueue.canMakePayments()) throw SubscriptionException.PaymentNotAllowed()
            check(pendingPurchase == null) { "A purchase is already in progress" }

            val payment = SKMutablePayment().apply {
                setProductIdentifier(offerId)
                setApplicationUsername(uuidFromKey(userId))
            }
            val result = CompletableDeferred<PurchaseResult>()
            val observer = PaymentObserver(
                targetId = offerId,
                result = result
            )
            val queue = SKPaymentQueue.defaultQueue()
            pendingPurchase = result

            try {
                queue.addTransactionObserver(observer)
                queue.addPayment(payment)
                result.await()
            } finally {
                queue.removeTransactionObserver(observer)
                result.cancel()
                pendingPurchase = null
            }
        }

    override suspend fun restorePurchase(): PurchaseResult.Purchased? =
        purchaseRestorer.restorePurchase(PRODUCT_ID)

    override suspend fun completePurchase(productId: String, purchaseToken: String) {
        storeKit.finishTransaction(purchaseToken)
    }

    companion object {
        private const val PRODUCT_ID = "premium_monthly_1"
    }
}

private class ProductsDelegate : NSObject(), SKProductsRequestDelegateProtocol {
    val result = CompletableDeferred<List<SKProduct>>()

    override fun productsRequest(
        request: SKProductsRequest,
        didReceiveResponse: SKProductsResponse
    ) {
        result.complete(didReceiveResponse.products as List<SKProduct>)
    }

    override fun request(
        request: SKRequest,
        didFailWithError: NSError
    ) {
        result.complete(emptyList())
    }
}

private class PaymentObserver(
    private val targetId: String,
    private val result: CompletableDeferred<PurchaseResult>
) : NSObject(), SKPaymentTransactionObserverProtocol {

    override fun paymentQueue(
        queue: SKPaymentQueue,
        updatedTransactions: List<*>
    ) {
        updatedTransactions
            .filterIsInstance<SKPaymentTransaction>()
            .filter { it.payment.productIdentifier == targetId }
            .forEach { tx ->
                handleTransaction(queue, tx)
            }
    }

    private fun handleTransaction(queue: SKPaymentQueue, transaction: SKPaymentTransaction) {
        if (result.isCompleted) return

        when (transaction.transactionState) {
            SKPaymentTransactionState.SKPaymentTransactionStatePurchased ->
                completePurchase(transaction.transactionIdentifier)
            SKPaymentTransactionState.SKPaymentTransactionStateRestored ->
                completePurchase(transaction.originalTransaction?.transactionIdentifier)
            SKPaymentTransactionState.SKPaymentTransactionStateFailed -> {
                val error = transaction.error
                if (error?.isPurchaseCancelled() == true) {
                    result.complete(PurchaseResult.Cancelled)
                } else {
                    result.completeExceptionally(error?.toSubscriptionException() ?: SubscriptionPurchaseException())
                }
                queue.finishTransaction(transaction)
            }
            SKPaymentTransactionState.SKPaymentTransactionStatePurchasing -> Unit
            SKPaymentTransactionState.SKPaymentTransactionStateDeferred -> Unit
            else -> result.completeExceptionally(SubscriptionPurchaseException())
        }
    }

    private fun completePurchase(transactionId: String?) {
        if (transactionId.isNullOrBlank()) {
            result.completeExceptionally(SubscriptionException.RecoveryFailed())
            return
        }

        result.complete(PurchaseResult.Purchased(targetId, transactionId))
    }
}
