package com.padabajka.dating.feature.profile.domain

import com.padabajka.dating.core.repository.api.ImageRepository
import com.padabajka.dating.core.repository.api.model.profile.Image

class LoadImageUseCase(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(images: List<Image>): List<Image> {
        return images.map {
            when (it) {
                is Image.ByteArray -> imageRepository.uploadImage(it)
                is Image.Local -> {
                    val image = imageRepository.getLocalImage(it)
                    imageRepository.uploadImage(image)
                }

                is Image.Url -> it
            }
        }
    }
}
