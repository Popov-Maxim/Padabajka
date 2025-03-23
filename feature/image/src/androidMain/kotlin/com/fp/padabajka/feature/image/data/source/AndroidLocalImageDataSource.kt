package com.fp.padabajka.feature.image.data.source

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.fp.padabajka.core.repository.api.model.profile.ImageData
import java.io.InputStream

class AndroidLocalImageDataSource(
    private val context: Context
) : LocalImageDataSource {
    override fun getImage(data: ImageData): ByteArray {
        return context.contentResolver.useInputStream(data.uri) { inputStream ->
            inputStream.readBytes()
        }
    }

    private fun <T> ContentResolver.useInputStream(uri: Uri, block: (InputStream) -> T): T {
        // TODO(Image): handle null input stream
        return this.openInputStream(uri)!!.use(block)
    }
}
