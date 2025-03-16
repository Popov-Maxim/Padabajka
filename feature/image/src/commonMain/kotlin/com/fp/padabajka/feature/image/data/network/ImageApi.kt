package com.fp.padabajka.feature.image.data.network

import com.fp.padabajka.core.data.network.model.ImageDto

interface ImageApi {
    suspend fun post(byteArray: ByteArray): ImageDto

    companion object {
        const val PATH = "upload_image/profile"
    }
}
