package com.padabajka.dating.feature.image.data.source

import com.padabajka.dating.core.data.network.model.ImageDto
import com.padabajka.dating.feature.image.data.network.ImageApi

class RemoveImageDataSourceImpl(private val imageApi: ImageApi) : RemoveImageDataSource {
    override suspend fun uploadImage(byteArray: ByteArray): ImageDto {
        return imageApi.post(byteArray)
    }
}
