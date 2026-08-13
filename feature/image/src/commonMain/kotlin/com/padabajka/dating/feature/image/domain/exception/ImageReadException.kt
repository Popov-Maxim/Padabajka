package com.padabajka.dating.feature.image.domain.exception

class ImageReadException(cause: Throwable? = null) : Exception(
    message = "Unable to read image file",
    cause = cause
)
