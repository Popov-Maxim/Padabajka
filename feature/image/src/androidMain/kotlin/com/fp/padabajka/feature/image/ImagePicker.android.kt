package com.fp.padabajka.feature.image

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fp.padabajka.core.repository.api.model.profile.Image

actual class ImagePickerImpl(
    private val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) : ImagePicker {
    override suspend fun pickImage() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
actual fun rememberImagePicker(onResult: (Image?) -> Unit): ImagePicker {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        onResult(uri?.toString()?.let { Image.LocalUri(it) })
    }
    return remember { ImagePickerImpl(launcher) }
}
