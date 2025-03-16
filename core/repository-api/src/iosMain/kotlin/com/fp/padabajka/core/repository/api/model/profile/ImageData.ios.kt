package com.fp.padabajka.core.repository.api.model.profile

import platform.UIKit.UIImage

actual interface ImageData {
    val uiImage: UIImage

    companion object {
        operator fun invoke(uiImage: UIImage): ImageData = ImageDataImpl(uiImage)
    }
}

private data class ImageDataImpl(override val uiImage: UIImage) : ImageData
