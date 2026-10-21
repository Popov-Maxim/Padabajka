package com.padabajka.dating.feature.image.data.source

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.padabajka.dating.core.repository.api.model.profile.Size
import com.padabajka.dating.feature.image.domain.exception.ImageReadException

internal data class AndroidImageOrientation(private val isFlipped: Boolean, private val rotationDegrees: Int) {
    fun displaySize(size: Size): Size =
        if (rotationDegrees % HALF_TURN != 0) Size(size.height, size.width) else size

    fun applyTo(bitmap: Bitmap): Bitmap {
        if (!isFlipped && rotationDegrees == 0) return bitmap
        val matrix = Matrix().apply {
            if (isFlipped) postScale(-1f, 1f)
            if (rotationDegrees != 0) postRotate(rotationDegrees.toFloat())
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        private const val HALF_TURN = 180
        private val NONE = AndroidImageOrientation(isFlipped = false, rotationDegrees = 0)

        // Match the formats normalized by Coil's default EXIF strategy in the crop preview.
        private val EXIF_MIME_TYPES = setOf("image/jpeg", "image/webp", "image/heic", "image/heif")

        fun read(resolver: ContentResolver, uri: Uri, mimeType: String?): AndroidImageOrientation {
            if (mimeType !in EXIF_MIME_TYPES) return NONE
            return resolver.openInputStream(uri)?.use { input ->
                val exif = ExifInterface(input)
                AndroidImageOrientation(exif.isFlipped, exif.rotationDegrees)
            } ?: throw ImageReadException()
        }
    }
}
