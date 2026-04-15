package com.padabajka.dating.feature.image

import com.padabajka.dating.core.repository.api.model.profile.ImageData
import com.padabajka.dating.core.repository.api.model.profile.Size
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage

data class ImageDataImpl(override val uiImage: UIImage) : ImageData {
    override val rawData: Any by lazy {
        uiImage.toByteArray()!!
    }

    @OptIn(ExperimentalForeignApi::class)
    override val size: Size
        get() = Size(
            (uiImage.size.width * uiImage.scale).toInt(),
            (uiImage.size.height * uiImage.scale).toInt(),
        )
}
