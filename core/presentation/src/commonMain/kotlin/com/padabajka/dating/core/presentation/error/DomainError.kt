package com.padabajka.dating.core.presentation.error

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.exception.BadStatusCodeException
import com.padabajka.dating.core.repository.api.exception.ConnectException
import com.padabajka.dating.core.repository.api.exception.UserException

sealed interface DomainError {
    companion object {
        fun mapError(e: Throwable): DomainError {
            return when (e) {
                is ConnectException -> ExternalDomainError.TextError.Internet
                is UserException -> InternalDomainError.User(e)
                is BadStatusCodeException -> ExternalDomainError.TextError.BadStatusCode
                else -> ExternalDomainError.Unknown(e)
            }
        }
    }
}

sealed interface InternalDomainError : DomainError {
    data class User(val error: UserException) : InternalDomainError
}

sealed class ExternalDomainError(open val needLog: Boolean) : DomainError {

    data class TextError(val text: StaticTextId, override val needLog: Boolean) : ExternalDomainError(needLog) {
        companion object {
            val Internet = TextError(StaticTextId.UiId.InternetConnectionErrorDescription, false)
            val BadStatusCode = TextError(StaticTextId.UiId.UnknownErrorDescription, false)
            val Unknown = TextError(StaticTextId.UiId.UnknownErrorDescription, true)
        }
    }

    data class Unknown(val e: Throwable) : ExternalDomainError(true)
}

fun StaticTextId.UiId.toTextError(needLog: Boolean = false): ExternalDomainError.TextError {
    return ExternalDomainError.TextError(this, needLog)
}
