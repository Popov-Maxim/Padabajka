package com.padabajka.dating.feature.image.data.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.padabajka.dating.feature.image.domain.ImageCompressor
import java.io.ByteArrayOutputStream
import kotlin.math.sqrt

class AndroidImageCompressor : ImageCompressor {

    override fun compress(
        data: ByteArray,
        config: ImageCompressor.Configuration
    ): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            ?: throw IllegalArgumentException("Cannot decode image bytes")

        val (newWidth, newHeight) = calculateNewDimensions(
            bitmap.width,
            bitmap.height,
            config.maxMegapixels
        )

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)

        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, config.quality, outputStream)

        return outputStream.toByteArray()
    }

    private fun calculateNewDimensions(
        width: Int,
        height: Int,
        maxMegapixels: Float
    ): Pair<Int, Int> {
        val currentMP = width.toFloat() * height / MEGAPIXEL
        println("LoadImageUseCase: currentMP $currentMP mb")
        if (currentMP <= maxMegapixels) return width to height

        val scale = sqrt(maxMegapixels / currentMP)
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        return newWidth to newHeight
    }

    companion object {
        private const val MEGAPIXEL = 1_000_000f
    }
}
