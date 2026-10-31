package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.core.repository.api.exception.SubscriptionPurchaseException
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
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
import kotlin.coroutines.resume

class IosBillingClient( // TODO(P0)
    private val skProductMapper: SKProductMapper,
    private val purchaseRestorer: IosPurchaseRestorer
) : PlatformBillingClient {

    private var pendingPurchase: CompletableDeferred<PurchaseResult>? = null

    override val serviceName =
        PlatformBillingClient.ServiceName.APP_STORE

    override suspend fun getSubscriptions(): List<SubscriptionProduct> {
        return suspendCancellableCoroutine { cont ->

            val request = SKProductsRequest(productIdentifiers = setOf(PRODUCT_ID))

            val delegate = ProductsDelegate(
                onResult = { products ->

                    val mapped = products.map { product ->
                        skProductMapper.map(product)
                    }

                    cont.resume(mapped)
                },
                onError = {
                    cont.resume(emptyList())
                }
            )

            request.delegate = delegate
            request.start()
        }
    }

    override suspend fun launchPurchase(offerId: String, userId: String): PurchaseResult =
        withContext(Dispatchers.Main.immediate) {
            if (!SKPaymentQueue.canMakePayments()) throw SubscriptionException.PaymentNotAllowed()
            check(pendingPurchase == null) { "A purchase is already in progress" }

            val payment = SKMutablePayment().apply {
                setProductIdentifier(offerId)
                setApplicationUsername(userId)
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

    companion object {
        private const val PRODUCT_ID = "premium_monthly_1"
    }
}

private class ProductsDelegate(
    private val onResult: (List<SKProduct>) -> Unit,
    private val onError: (NSError) -> Unit
) : NSObject(), SKProductsRequestDelegateProtocol {

    override fun productsRequest(
        request: SKProductsRequest,
        didReceiveResponse: SKProductsResponse
    ) {
        onResult(didReceiveResponse.products as List<SKProduct>)
    }

    override fun request(
        request: SKRequest,
        didFailWithError: NSError
    ) {
        onError(didFailWithError)
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
                completePurchase(queue, transaction, transaction.transactionIdentifier)
            SKPaymentTransactionState.SKPaymentTransactionStateRestored ->
                completePurchase(queue, transaction, transaction.originalTransaction?.transactionIdentifier)
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

    private fun completePurchase(
        queue: SKPaymentQueue,
        transaction: SKPaymentTransaction,
        transactionId: String?
    ) {
        if (transactionId.isNullOrBlank()) {
            result.completeExceptionally(SubscriptionException.RecoveryFailed())
            return
        }

        result.complete(PurchaseResult.Purchased(targetId, transactionId))
        queue.finishTransaction(transaction)
    }
}
