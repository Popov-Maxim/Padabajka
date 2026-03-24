package com.padabajka.dating.feature.image.domain

import coil3.Bitmap
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.toBitmap
import com.padabajka.dating.feature.image.allowHardwarePlatform

class LoadBitmapUseCase(
    private val platformContext: PlatformContext,
    private val imageLoader: ImageLoader
) {
    suspend fun invoke(url: String): Bitmap? {
        val request = ImageRequest.Builder(platformContext)
            .data(url)
            .allowHardwarePlatform(false)
            .build()

        val result = imageLoader.execute(request)

        return result.image?.toBitmap()
    }
}
