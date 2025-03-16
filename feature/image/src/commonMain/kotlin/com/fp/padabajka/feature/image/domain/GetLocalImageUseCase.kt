package com.fp.padabajka.feature.image.domain

import com.fp.padabajka.core.repository.api.ImageRepository
import com.fp.padabajka.core.repository.api.model.profile.Image

class GetLocalImageUseCase(private val imageRepository: ImageRepository) {
    suspend operator fun invoke(localImage: Image.Local): Image.ByteArray {
        return imageRepository.getLocalImage(localImage)
    }
}
