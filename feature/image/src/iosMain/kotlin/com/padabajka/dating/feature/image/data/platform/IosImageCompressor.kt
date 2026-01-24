package com.padabajka.dating.feature.image.data.platform

import com.padabajka.dating.feature.image.data.SizeUtils
import com.padabajka.dating.feature.image.domain.ImageCompressor
import com.padabajka.dating.feature.image.height
import com.padabajka.dating.feature.image.toByteArray
import com.padabajka.dating.feature.image.toNSData
import com.padabajka.dating.feature.image.width
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat
import platform.UIKit.UIImage

class IosImageCompressor : ImageCompressor {

    override fun compress(
        data: ByteArray,
        config: ImageCompressor.Configuration
    ): ByteArray {
        val nsData = data.toNSData()
        val image = UIImage.imageWithData(nsData) ?: return data

        val resizedImage = resizeIfNeeded(image, config.maxMegapixels)

        val quality = config.quality.toDouble() / FULL_PERCENT
        return resizedImage.toByteArray(quality = quality)
            ?: data
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun resizeIfNeeded(
        image: UIImage,
        maxMegapixels: Float
    ): UIImage {
        val size = image.size
        val width = size.width
        val height = size.height

        val newSize = SizeUtils.calculateNewDimensions(
            width.toInt(),
            height.toInt(),
            maxMegapixels
        )

        if (newSize == width to height) {
            return image
        } else {
            val newWidth = newSize.first.toDouble()
            val newHeight = newSize.second.toDouble()

            return image.scale(newWidth, newHeight)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun UIImage.scale(newWidth: Double, newHeight: Double): UIImage {
        val rendererFormat = UIGraphicsImageRendererFormat.defaultFormat().apply {
            this.scale = 1.0
            this.opaque = false
        }
        val renderer = UIGraphicsImageRenderer(
            size = CGSizeMake(newWidth, newHeight),
            format = rendererFormat
        )

        return renderer.imageWithActions { _ ->
            drawInRect(
                CGRectMake(0.0, 0.0, newWidth, newHeight)
            )
        }
    }

    companion object {
        private const val FULL_PERCENT = 100
    }
}
