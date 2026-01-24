package com.padabajka.dating.feature.image.domain

import com.padabajka.dating.core.repository.api.ImageRepository
import com.padabajka.dating.core.repository.api.model.profile.Image

class LoadImageUseCase(
    private val imageRepository: ImageRepository,
    private val imageCompressor: ImageCompressor
) {

    suspend operator fun invoke(images: List<Image>): List<Image> {
        return images.map {
            when (it) {
                is Image.ByteArray -> uploadImage(it)
                is Image.Local -> {
                    val image = imageRepository.getLocalImage(it)
                    uploadImage(image)
                }

                is Image.Url -> it
            }
        }
    }

    private suspend fun uploadImage(byteArray: Image.ByteArray): Image.Url {
        val compressedImage = imageCompressor.compress(byteArray.value, COMPRESS_CONFIGURATION)
        val image = Image.ByteArray(compressedImage)

        println("LoadImageUseCase: compressing ${byteArray.sizeInKb()} kb -> ${image.sizeInKb()} kb")
        return imageRepository.uploadImage(image)
    }

    private fun Image.ByteArray.sizeInKb(): Double = this.value.size.toDouble() / KB_IN_BYTE

    companion object {
        private val COMPRESS_CONFIGURATION = ImageCompressor.Configuration(4f, 70)
        private const val KB_IN_BYTE = 1024
    }
}
