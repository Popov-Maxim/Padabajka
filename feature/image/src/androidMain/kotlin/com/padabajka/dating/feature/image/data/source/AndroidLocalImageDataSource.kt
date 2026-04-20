package com.padabajka.dating.feature.image.data.source

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.padabajka.dating.core.repository.api.model.common.CoreRect
import com.padabajka.dating.core.repository.api.model.profile.ImageData
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AndroidLocalImageDataSource(
    private val context: Context
) : LocalImageDataSource {
    override fun getImage(data: ImageData, cropRect: CoreRect?): ByteArray {
        return context.contentResolver.useInputStream(data.uri) { inputStream ->
            if (cropRect == null) {
                inputStream.readBytes()
            } else {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val cropped = Bitmap.createBitmap(
                    bitmap,
                    cropRect.left,
                    cropRect.top,
                    cropRect.right - cropRect.left,
                    cropRect.bottom - cropRect.top
                )
                cropped.toByteArray()
            }
        }
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, MAX_QUALITY, stream)
        return stream.toByteArray()
    }

    private fun <T> ContentResolver.useInputStream(uri: Uri, block: (InputStream) -> T): T {
        return this.openInputStream(uri)?.use(block) ?: TODO("Image: handle null input stream") // TODO(P1)
    }

    companion object {
        private const val MAX_QUALITY = 100
    }
}
