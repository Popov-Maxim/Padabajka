package com.padabajka.dating.feature.image

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.ImageData

class ImagePickerImpl(
    private val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) : ImagePicker {
    override suspend fun pickImage() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
actual fun rememberImagePicker(onResult: (Image?) -> Unit): ImagePicker {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        val image = uri
            ?.run { ImageData(this) }
            ?.run { Image.Local(this) }
        onResult(image)
    }
    return remember { ImagePickerImpl(launcher) }
}
