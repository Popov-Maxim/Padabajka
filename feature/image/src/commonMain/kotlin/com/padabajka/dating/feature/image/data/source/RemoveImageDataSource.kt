package com.padabajka.dating.feature.image.data.source

import com.padabajka.dating.core.data.network.model.ImageDto

interface RemoveImageDataSource {
    suspend fun uploadImage(byteArray: ByteArray): ImageDto
}
