package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.StoreKit.SKPayment
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
    private val skProductMapper: SKProductMapper
) : PlatformBillingClient {

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

    override suspend fun launchPurchase(offerId: String): PurchaseResult? {
        return suspendCancellableCoroutine { cont ->

            val payment = SKPayment.paymentWithProductIdentifier(offerId) as SKPayment

            val observer = PaymentObserver(
                targetId = offerId
            ) { result ->
                cont.resume(result)
            }

            SKPaymentQueue.defaultQueue().addTransactionObserver(observer)
            SKPaymentQueue.defaultQueue().addPayment(payment)
        }
    }

    companion object {
        private const val PRODUCT_ID = "premium_monthly"
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
    private val onResult: (PurchaseResult) -> Unit
) : NSObject(), SKPaymentTransactionObserverProtocol {

    override fun paymentQueue(
        queue: SKPaymentQueue,
        updatedTransactions: List<*>
    ) {
        updatedTransactions
            .map { it as SKPaymentTransaction }
            .forEach { tx ->

                when (tx.transactionState) {
                    SKPaymentTransactionState.SKPaymentTransactionStatePurchased -> {
                        SKPaymentQueue.defaultQueue().finishTransaction(tx)

                        if (tx.payment.productIdentifier == targetId) {
                            onResult(PurchaseResult(targetId))
                        }
                    }

                    SKPaymentTransactionState.SKPaymentTransactionStateFailed -> {
                        SKPaymentQueue.defaultQueue().finishTransaction(tx)

                        val error = tx.error?.localizedDescription ?: "Unknown error"
//                        onResult(PurchaseResult.Error(error))
                    }

                    SKPaymentTransactionState.SKPaymentTransactionStateRestored -> {
                        SKPaymentQueue.defaultQueue().finishTransaction(tx)
                    }

                    SKPaymentTransactionState.SKPaymentTransactionStatePurchasing -> Unit
                    SKPaymentTransactionState.SKPaymentTransactionStateDeferred -> Unit
                    else -> Unit
                }
            }
    }
}
