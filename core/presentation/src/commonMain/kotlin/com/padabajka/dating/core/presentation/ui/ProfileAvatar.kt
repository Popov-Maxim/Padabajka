package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.padabajka.dating.core.presentation.ui.utils.rememberImageLoader

@Composable
fun ProfileAvatar(
    model: Any?,
    modifier: Modifier = Modifier.size(80.dp),
    onClick: (() -> Unit)? = null
) {
    val imageLoader = rememberImageLoader()

    val imageShape = CircleShape
    val optionalClickable: Modifier.() -> Modifier =
        { if (onClick != null) clickable(onClick = onClick) else this }
    AsyncImage(
        model = model,
        modifier = modifier.background(
            color = Color.DarkGray,
            shape = imageShape
        ).clip(imageShape).optionalClickable(),
        imageLoader = imageLoader,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}
