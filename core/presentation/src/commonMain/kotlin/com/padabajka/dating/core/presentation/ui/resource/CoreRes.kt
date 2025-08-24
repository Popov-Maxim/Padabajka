package com.padabajka.dating.core.presentation.ui.resource

import androidx.compose.runtime.Composable
import coil3.compose.rememberAsyncImagePainter
import com.padabajka.dating.core.presentation.ui.utils.rememberImageLoader

object CoreRes {
    object svg
    object img
}

@Composable
internal fun painterResource(path: String) =
    rememberAsyncImagePainter(
        model = path,
        imageLoader = rememberImageLoader(),
    )
