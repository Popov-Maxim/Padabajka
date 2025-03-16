package com.fp.padabajka.feature.image.data.source

import com.fp.padabajka.core.data.network.model.ImageDto

interface RemoveImageDataSource {
    suspend fun uploadImage(byteArray: ByteArray): ImageDto
}
