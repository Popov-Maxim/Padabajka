@file:Suppress("Filename", "AnnotationOnSeparateLine")
package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.repository.api.exception.NativeAdException

data class LoadErrorException(override val message: String) : NativeAdException(message)
