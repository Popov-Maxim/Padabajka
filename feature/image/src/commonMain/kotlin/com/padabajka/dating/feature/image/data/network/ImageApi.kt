package com.padabajka.dating.feature.image.data.network

import com.padabajka.dating.core.data.network.model.ImageDto

interface ImageApi {
    suspend fun post(byteArray: ByteArray): ImageDto

    companion object {
        const val PATH = "upload_image/profile"
    }
}
