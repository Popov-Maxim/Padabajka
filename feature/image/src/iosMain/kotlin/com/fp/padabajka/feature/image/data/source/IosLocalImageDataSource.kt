package com.fp.padabajka.feature.image.data.source

import com.fp.padabajka.core.repository.api.model.profile.ImageData
import com.fp.padabajka.feature.image.toByteArray

class IosLocalImageDataSource : LocalImageDataSource {
    override fun getImage(data: ImageData): ByteArray {
        // TODO(Image): handle null byteArray
        val byteArray = data.uiImage.toByteArray()!!
        return byteArray
    }
}
