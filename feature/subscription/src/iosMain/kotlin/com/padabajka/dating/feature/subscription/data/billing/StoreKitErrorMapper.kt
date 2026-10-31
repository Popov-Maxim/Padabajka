package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.core.repository.api.exception.SubscriptionPurchaseException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorDomain
import platform.StoreKit.SKErrorCode
import platform.StoreKit.SKErrorDomain

internal fun NSError.isPurchaseCancelled(): Boolean =
    domain == SKErrorDomain && code == SKErrorCode.SKErrorPaymentCancelled.value

internal fun NSError.toSubscriptionException(): SubscriptionException = when (domain) {
    NSURLErrorDomain -> SubscriptionException.StoreUnavailable()
    SKErrorDomain -> toStoreKitException()
    else -> SubscriptionPurchaseException()
}

private fun NSError.toStoreKitException(): SubscriptionException = when (code) {
    SKErrorCode.SKErrorClientInvalid.value,
    SKErrorCode.SKErrorPaymentNotAllowed.value -> SubscriptionException.PaymentNotAllowed()
    SKErrorCode.SKErrorStoreProductNotAvailable.value,
    SKErrorCode.SKErrorIneligibleForOffer.value -> SubscriptionException.ProductUnavailable()
    SKErrorCode.SKErrorCloudServiceNetworkConnectionFailed.value,
    SKErrorCode.SKErrorOverlayTimeout.value -> SubscriptionException.StoreUnavailable()
    SKErrorCode.SKErrorPaymentInvalid.value,
    SKErrorCode.SKErrorInvalidOfferIdentifier.value,
    SKErrorCode.SKErrorInvalidSignature.value,
    SKErrorCode.SKErrorMissingOfferParams.value,
    SKErrorCode.SKErrorInvalidOfferPrice.value -> SubscriptionException.ConfigurationError()
    else -> SubscriptionPurchaseException()
}
