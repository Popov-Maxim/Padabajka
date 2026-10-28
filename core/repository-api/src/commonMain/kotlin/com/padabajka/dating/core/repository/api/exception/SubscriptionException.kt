package com.padabajka.dating.core.repository.api.exception

sealed class SubscriptionException(message: String) : Exception(message) {
    class StoreUnavailable : SubscriptionException("Subscription store is unavailable")
    class ProductUnavailable : SubscriptionException("Subscription product is unavailable")
    class PaymentNotAllowed : SubscriptionException("Subscription payment is not allowed")
    class RecoveryFailed : SubscriptionException("Subscription purchase could not be recovered")
    class ActivationFailed : SubscriptionException("Subscription activation could not be confirmed")
    class VerificationUnavailable : SubscriptionException("Subscription verification is unavailable")
    class PurchaseNotFound : SubscriptionException("Subscription purchase was not found")
    class InvalidPurchase : SubscriptionException("Subscription purchase is not valid")
    class ConfigurationError : SubscriptionException("Subscription configuration is invalid")
}

class SubscriptionPurchaseException : SubscriptionException("Subscription purchase failed")

class SubscriptionAlreadyLinkedException : SubscriptionException("Subscription belongs to another account")
