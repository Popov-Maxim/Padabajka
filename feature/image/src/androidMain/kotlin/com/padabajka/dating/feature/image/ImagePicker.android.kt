package com.padabajka.dating.feature.image

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.ImageData
import com.padabajka.dating.core.repository.api.model.profile.Size

class ImagePickerImpl(
    private val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) : ImagePicker {
    override suspend fun pickImage() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
actual fun rememberImagePicker(onResult: (Image.Local?) -> Unit): ImagePicker {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri == null) return@rememberLauncherForActivityResult onResult(null)

        val size = context.contentResolver.openInputStream(uri)?.use { input ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(input, null, options)

            Size(options.outWidth, options.outHeight)
        } ?: return@rememberLauncherForActivityResult onResult(null)

        val image = uri
            .run { ImageData(this, size) }
            .run { Image.Local(this) }
        onResult(image)
    }
    return remember { ImagePickerImpl(launcher) }
}
