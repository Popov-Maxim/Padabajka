package com.padabajka.dating.feature.image.domain

interface ImageCompressor {
    fun compress(data: ByteArray, config: Configuration): ByteArray

    data class Configuration(
        val maxMegapixels: Float,
        val quality: Int,
    )
}
