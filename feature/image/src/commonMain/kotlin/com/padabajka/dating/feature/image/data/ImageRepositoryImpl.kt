package com.padabajka.dating.feature.image.data

import com.padabajka.dating.core.data.network.model.toImage
import com.padabajka.dating.core.repository.api.ImageRepository
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.feature.image.data.source.LocalImageDataSource
import com.padabajka.dating.feature.image.data.source.RemoveImageDataSource

class ImageRepositoryImpl(
    private val removeImageDataSource: RemoveImageDataSource,
    private val localImageDataSource: LocalImageDataSource
) : ImageRepository {
    override suspend fun uploadImage(byteArray: Image.ByteArray): Image.Url {
        return removeImageDataSource.uploadImage(byteArray.value).toImage()
    }

    override suspend fun getLocalImage(image: Image.Local): Image.ByteArray {
        val array = localImageDataSource.getImage(image.data)
        return Image.ByteArray(array)
    }
}
