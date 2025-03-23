package com.fp.padabajka.feature.image.data.source

import com.fp.padabajka.core.repository.api.model.profile.ImageData

interface LocalImageDataSource {
    fun getImage(data: ImageData): ByteArray
}
