package com.padabajka.dating.feature.image.data.source

import com.padabajka.dating.core.repository.api.model.common.CoreRect
import com.padabajka.dating.core.repository.api.model.profile.ImageData
import com.padabajka.dating.feature.image.toByteArray
import com.padabajka.dating.feature.image.toImageRect
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGRect
import platform.UIKit.UIImage

class IosLocalImageDataSource : LocalImageDataSource {
    @OptIn(ExperimentalForeignApi::class)
    override fun getImage(data: ImageData, cropRect: CoreRect?): ByteArray {
        val croppedImage = if (cropRect == null) {
            data.uiImage
        } else {
            val rect = cropRect.toImageRect(data.uiImage.scale)
            data.uiImage.crop(rect)
        }
        val byteArray = croppedImage.toByteArray() ?: TODO("Image: handle null input stream")
        return byteArray
    }
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("ReturnCount")
private fun UIImage.crop(rect: CValue<CGRect>): UIImage {
    val cgImage = this.CGImage ?: return this

    val cropped = CGImageCreateWithImageInRect(cgImage, rect)
        ?: return this

    return UIImage.imageWithCGImage(cropped, scale = this.scale, orientation = this.imageOrientation)
}
