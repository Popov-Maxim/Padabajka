package com.fp.padabajka.feature.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fp.padabajka.core.repository.api.model.profile.Image
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

actual class ImagePickerImpl(
    private val onLaunch: () -> Unit
) : ImagePicker {
    override suspend fun pickImage() {
        onLaunch()
    }
}

@Composable
actual fun rememberImagePicker(onResult: (Image?) -> Unit): ImagePicker {
    val imagePicker = remember {
        UIImagePickerController()
    }
    val galleryDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val uiImage = didFinishPickingMediaWithInfo.getValue(
                    UIImagePickerControllerEditedImage
                ) as? UIImage ?: didFinishPickingMediaWithInfo.getValue(
                    UIImagePickerControllerOriginalImage
                ) as? UIImage
                val byteArray = uiImage?.toByteArray()
                val image = byteArray?.let { Image.ByteArray(byteArray) }

                onResult.invoke(image)
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }
    return remember {
        ImagePickerImpl {
            imagePicker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
            imagePicker.setAllowsEditing(true)
            imagePicker.setDelegate(galleryDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                imagePicker,
                true,
                null
            )
        }
    }
}
