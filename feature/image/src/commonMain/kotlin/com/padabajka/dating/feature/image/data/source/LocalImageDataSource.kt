package com.padabajka.dating.feature.image.data.source

import com.padabajka.dating.core.repository.api.model.profile.ImageData

interface LocalImageDataSource {
    fun getImage(data: ImageData): ByteArray
}
