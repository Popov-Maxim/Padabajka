package com.padabajka.dating.feature.image.data.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import com.padabajka.dating.feature.image.data.SizeUtils
import com.padabajka.dating.feature.image.domain.ImageCompressor
import com.padabajka.dating.feature.image.domain.exception.ImageReadException
import java.io.ByteArrayOutputStream

class AndroidImageCompressor : ImageCompressor {

    override fun compress(
        data: ByteArray,
        config: ImageCompressor.Configuration
    ): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            ?: throw ImageReadException()

        val newSize = SizeUtils.calculateNewDimensions(
            bitmap.width,
            bitmap.height,
            config.maxMegapixels
        )

        val resizedBitmap = if (newSize != bitmap.width to bitmap.height) {
            val (newWidth, newHeight) = newSize
            bitmap.scale(newWidth, newHeight)
        } else {
            bitmap
        }

        val outputStream = ByteArrayOutputStream()
        val compressed = resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            config.quality,
            outputStream
        )
        if (compressed.not()) throw ImageReadException()

        return outputStream.toByteArray()
    }
}
