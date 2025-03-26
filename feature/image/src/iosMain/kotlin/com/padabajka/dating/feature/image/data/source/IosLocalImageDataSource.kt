package com.padabajka.dating.feature.image.data.source

import com.padabajka.dating.core.repository.api.model.profile.ImageData
import com.padabajka.dating.feature.image.toByteArray

class IosLocalImageDataSource : LocalImageDataSource {
    override fun getImage(data: ImageData): ByteArray {
        // TODO(Image): handle null byteArray
        val byteArray = data.uiImage.toByteArray()!!
        return byteArray
    }
}
