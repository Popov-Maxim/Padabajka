package com.padabajka.dating.feature.image

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGSize
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

internal fun UIImage.toByteArray(format: String = "jpg", quality: Double = 1.0): ByteArray? {
    val nsData = when (format.lowercase()) {
        "jpg", "jpeg" -> UIImageJPEGRepresentation(this, quality)
        "png" -> UIImagePNGRepresentation(this)
        else -> null
    } ?: return null

    return nsData.toByteArray()
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    return ByteArray(this@toByteArray.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun ByteArray.toNSData(): NSData =
    this.usePinned {
        NSData.dataWithBytes(it.addressOf(0), size.toULong())
    }

@OptIn(ExperimentalForeignApi::class)
val CValue<CGSize>.width: Double
    get() = useContents { this.width }

@OptIn(ExperimentalForeignApi::class)
val CValue<CGSize>.height: Double
    get() = useContents { this.height }
