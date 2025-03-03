package com.fp.padabajka.feature.image

import androidx.compose.runtime.Composable
import com.fp.padabajka.core.repository.api.model.profile.Image

interface ImagePicker {
    suspend fun pickImage()
}

expect class ImagePickerImpl : ImagePicker

@Composable
expect fun rememberImagePicker(onResult: (Image?) -> Unit): ImagePicker
