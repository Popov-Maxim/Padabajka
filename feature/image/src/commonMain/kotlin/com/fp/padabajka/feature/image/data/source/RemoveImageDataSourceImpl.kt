package com.fp.padabajka.feature.image.data.source

import com.fp.padabajka.core.data.network.model.ImageDto
import com.fp.padabajka.feature.image.data.network.ImageApi

class RemoveImageDataSourceImpl(private val imageApi: ImageApi) : RemoveImageDataSource {
    override suspend fun uploadImage(byteArray: ByteArray): ImageDto {
        return imageApi.post(byteArray)
    }
}
