package com.padabajka.dating.feature.image.data.source

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.padabajka.dating.core.repository.api.model.common.CoreRect
import com.padabajka.dating.core.repository.api.model.profile.ImageData
import com.padabajka.dating.feature.image.domain.exception.ImageReadException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class AndroidLocalImageDataSource(
    private val context: Context
) : LocalImageDataSource {
    override fun getImage(data: ImageData, cropRect: CoreRect?): ByteArray {
        return context.contentResolver.useInputStream(data.uri) { inputStream ->
            if (cropRect == null) {
                inputStream.readBytes()
            } else {
                val options = BitmapFactory.Options()
                val source = BitmapFactory.decodeStream(inputStream, null, options)
                    ?: throw ImageReadException()
                source.useBitmap { bitmap ->
                    val orientation = AndroidImageOrientation.read(
                        context.contentResolver,
                        data.uri,
                        options.outMimeType,
                    )
                    val normalized = orientation.applyTo(bitmap)
                    if (normalized === bitmap) {
                        bitmap.cropToByteArray(cropRect)
                    } else {
                        bitmap.recycle()
                        normalized.useBitmap { it.cropToByteArray(cropRect) }
                    }
                }
            }
        }
    }

    private fun Bitmap.cropToByteArray(rect: CoreRect): ByteArray {
        val cropped = Bitmap.createBitmap(this, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top)
        return if (cropped === this) toByteArray() else cropped.useBitmap { it.toByteArray() }
    }

    private inline fun <T> Bitmap.useBitmap(action: (Bitmap) -> T): T = try {
        action(this)
    } finally {
        recycle()
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        val compressed = this.compress(Bitmap.CompressFormat.JPEG, MAX_QUALITY, stream)
        if (compressed.not()) throw ImageReadException()
        return stream.toByteArray()
    }

    private fun <T> ContentResolver.useInputStream(uri: Uri, block: (InputStream) -> T): T {
        return try {
            openInputStream(uri)?.use(block) ?: throw ImageReadException()
        } catch (error: ImageReadException) {
            throw error
        } catch (error: IOException) {
            throw ImageReadException(error)
        } catch (error: SecurityException) {
            throw ImageReadException(error)
        }
    }

    companion object {
        private const val MAX_QUALITY = 100
    }
}
