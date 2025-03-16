package com.fp.padabajka.feature.image.data

import com.fp.padabajka.core.data.network.model.toImage
import com.fp.padabajka.core.repository.api.ImageRepository
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.feature.image.data.source.LocalImageDataSource
import com.fp.padabajka.feature.image.data.source.RemoveImageDataSource

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
