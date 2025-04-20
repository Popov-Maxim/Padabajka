package com.padabajka.dating.core.presentation.ui.utils

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalPlatformContext.current
    val imageLoader: ImageLoader = koinInject { parametersOf(context) }

    return imageLoader
}
