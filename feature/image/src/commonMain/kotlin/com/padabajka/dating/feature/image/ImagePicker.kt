package com.padabajka.dating.feature.image

import androidx.compose.runtime.Composable
import com.padabajka.dating.core.repository.api.model.profile.Image

interface ImagePicker {
    suspend fun pickImage()
}

@Composable
expect fun rememberImagePicker(onResult: (Image?) -> Unit): ImagePicker
