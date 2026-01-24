package com.padabajka.dating.feature.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.ImageData
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

class ImagePickerImpl(
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
                val uiImage = didFinishPickingMediaWithInfo.get(
                    UIImagePickerControllerEditedImage
                ) as? UIImage ?: didFinishPickingMediaWithInfo.getValue(
                    UIImagePickerControllerOriginalImage
                ) as? UIImage
                val image = uiImage
                    ?.run { ImageData(this) }
                    ?.run { Image.Local(this) }

                onResult.invoke(image)
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }
    return remember {
        ImagePickerImpl {
            imagePicker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
            imagePicker.setAllowsEditing(false)
            imagePicker.setDelegate(galleryDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                imagePicker,
                true,
                null
            )
        }
    }
}
