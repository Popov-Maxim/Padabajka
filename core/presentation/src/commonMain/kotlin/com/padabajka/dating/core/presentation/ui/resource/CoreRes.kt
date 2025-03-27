package com.padabajka.dating.core.presentation.ui.resource

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

object CoreRes {
    object svg
}

private val imageLoader: ImageLoader
    @Composable
    get() {
        val context = LocalPlatformContext.current
        return koinInject { parametersOf(context) }
    }

@Composable
internal fun painterResource(path: String) =
    rememberAsyncImagePainter(
        model = path,
        imageLoader = imageLoader,
    )
