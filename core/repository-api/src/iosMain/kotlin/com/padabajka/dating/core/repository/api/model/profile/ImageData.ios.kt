package com.padabajka.dating.core.repository.api.model.profile

import platform.UIKit.UIImage

actual interface ImageData {
    val uiImage: UIImage

    actual val rawData: Any

    actual val size: Size
}
