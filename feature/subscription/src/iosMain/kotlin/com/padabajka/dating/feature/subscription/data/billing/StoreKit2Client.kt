package com.padabajka.dating.feature.subscription.data.billing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import platform.Foundation.NSError

/** Emits on main; a null verification error means verified. Returns a cancellation function. */
typealias ObserveStoreKit2Entitlements = (
    onTransaction: (productId: String, transactionId: String, verificationError: NSError?) -> Unit,
    onComplete: () -> Unit
) -> (() -> Unit)

data class StoreKit2Transaction(
    val productId: String,
    val id: String
)

data class StoreKit2VerificationResult(
    val transaction: StoreKit2Transaction,
    val verificationError: NSError?
)

class StoreKit2Client(
    private val observeCurrentEntitlements: ObserveStoreKit2Entitlements
) {
    fun currentEntitlements(): Flow<StoreKit2VerificationResult> = callbackFlow {
        val cancel = observeCurrentEntitlements(
            { productId, transactionId, verificationError ->
                trySend(
                    StoreKit2VerificationResult(
                        transaction = StoreKit2Transaction(productId, transactionId),
                        verificationError = verificationError
                    )
                )
            },
            { close() }
        )
        awaitClose { cancel() }
    }
        // The finite snapshot must reach Kotlin in full, even if Swift emits it without suspending.
        .buffer(Channel.UNLIMITED)
        .flowOn(Dispatchers.Main.immediate)
}
