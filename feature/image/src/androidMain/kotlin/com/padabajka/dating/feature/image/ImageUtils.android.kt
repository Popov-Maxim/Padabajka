package com.padabajka.dating.feature.image

import coil3.request.ImageRequest
import coil3.request.allowHardware

actual fun ImageRequest.Builder.allowHardwarePlatform(allow: Boolean): ImageRequest.Builder {
    return this.allowHardware(allow)
}
