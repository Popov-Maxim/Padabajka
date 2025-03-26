package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.profile.Image

interface ImageRepository {
    suspend fun uploadImage(byteArray: Image.ByteArray): Image.Url
    suspend fun getLocalImage(image: Image.Local): Image.ByteArray
}
