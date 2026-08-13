package com.padabajka.dating.feature.image.presentation

import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.feature.image.domain.exception.ImageReadException

fun mapImageError(error: Throwable): ExternalDomainError.TextError? {
    return when (error) {
        is ImageReadException -> StaticTextId.UiId.ImageReadErrorDescription.toTextError()
        else -> null
    }
}
