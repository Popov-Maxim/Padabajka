package com.padabajka.dating.feature.subscription.presentation

import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.exception.SubscriptionAlreadyLinkedException
import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.core.repository.api.exception.SubscriptionPurchaseException

internal fun SubscriptionException.toTextError(): ExternalDomainError.TextError = when (this) {
    is SubscriptionException.StoreUnavailable ->
        StaticTextId.UiId.SubscriptionStoreUnavailableDescription.toTextError()

    is SubscriptionException.ProductUnavailable ->
        StaticTextId.UiId.SubscriptionProductUnavailableDescription.toTextError()

    is SubscriptionException.PaymentNotAllowed ->
        StaticTextId.UiId.SubscriptionPaymentNotAllowedDescription.toTextError()

    is SubscriptionPurchaseException ->
        StaticTextId.UiId.SubscriptionPurchaseFailedDescription.toTextError()

    is SubscriptionException.RecoveryFailed ->
        StaticTextId.UiId.SubscriptionRecoveryFailedDescription.toTextError()

    is SubscriptionException.ActivationFailed ->
        StaticTextId.UiId.SubscriptionActivationFailedDescription.toTextError()

    is SubscriptionException.VerificationUnavailable ->
        StaticTextId.UiId.SubscriptionVerificationUnavailableDescription.toTextError()

    is SubscriptionException.PurchaseNotFound ->
        StaticTextId.UiId.SubscriptionPurchaseNotFoundDescription.toTextError()

    is SubscriptionException.InvalidPurchase ->
        StaticTextId.UiId.SubscriptionInvalidPurchaseDescription.toTextError()

    is SubscriptionAlreadyLinkedException ->
        StaticTextId.UiId.SubscriptionAlreadyLinkedDescription.toTextError()

    is SubscriptionException.ConfigurationError ->
        StaticTextId.UiId.SubscriptionConfigurationErrorDescription.toTextError(needLog = true)
}
