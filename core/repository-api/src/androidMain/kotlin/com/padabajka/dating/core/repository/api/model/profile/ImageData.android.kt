package com.padabajka.dating.core.repository.api.model.profile

import android.net.Uri

actual interface ImageData {
    val uri: Uri

    actual val rawData: Any

    companion object {
        operator fun invoke(uri: Uri, size: Size): ImageData {
            return ImageDataImpl(uri, size)
        }
    }

    actual val size: Size
}

private data class ImageDataImpl(
    override val uri: Uri,
    override val size: Size
) : ImageData {
    override val rawData: Any
        get() = uri
}
