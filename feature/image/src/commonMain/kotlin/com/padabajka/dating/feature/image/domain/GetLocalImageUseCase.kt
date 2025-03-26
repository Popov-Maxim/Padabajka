package com.padabajka.dating.feature.image.domain

import com.padabajka.dating.core.repository.api.ImageRepository
import com.padabajka.dating.core.repository.api.model.profile.Image

class GetLocalImageUseCase(private val imageRepository: ImageRepository) {
    suspend operator fun invoke(localImage: Image.Local): Image.ByteArray {
        return imageRepository.getLocalImage(localImage)
    }
}
