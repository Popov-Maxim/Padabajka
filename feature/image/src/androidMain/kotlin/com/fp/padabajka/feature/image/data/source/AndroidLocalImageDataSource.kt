package com.fp.padabajka.feature.image.data.source

import android.content.Context
import com.fp.padabajka.core.repository.api.model.profile.ImageData

class AndroidLocalImageDataSource(
    private val context: Context
) : LocalImageDataSource {
    override fun getImage(data: ImageData): ByteArray {
        // TODO(Image): handle null input stream
        return context.contentResolver.openInputStream(data.uri)!!.use { inputStream ->
            inputStream.readBytes()
        }
    }
}
