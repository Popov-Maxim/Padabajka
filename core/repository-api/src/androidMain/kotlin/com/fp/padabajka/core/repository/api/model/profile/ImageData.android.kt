package com.fp.padabajka.core.repository.api.model.profile

import android.net.Uri

actual interface ImageData {
    val uri: Uri

    companion object {
        operator fun invoke(uri: Uri): ImageData {
            return ImageDataImpl(uri)
        }
    }
}

private data class ImageDataImpl(override val uri: Uri) : ImageData
