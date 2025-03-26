@file:Suppress("Filename", "AnnotationOnSeparateLine")
package com.padabajka.dating.feature.ads.data.source

import com.padabajka.dating.core.repository.api.exception.NativeAdException

data class LoadErrorException(override val message: String) : NativeAdException(message)
