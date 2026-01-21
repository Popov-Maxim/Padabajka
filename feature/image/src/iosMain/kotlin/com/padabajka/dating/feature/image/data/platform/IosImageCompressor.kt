package com.padabajka.dating.feature.image.data.platform

import com.padabajka.dating.feature.image.domain.ImageCompressor

class IosImageCompressor : ImageCompressor {

    override fun compress(
        data: ByteArray,
        config: ImageCompressor.Configuration
    ): ByteArray {
        return data
    }
}
