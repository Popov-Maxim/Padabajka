package com.padabajka.dating.core.presentation.ui.image

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.padabajka.dating.core.presentation.ui.utils.rememberImageLoader

@Composable
fun CoreAsyncImage(
    model: Any?,
    modifier: Modifier = Modifier,
    modifierForPlaceholder: Modifier = Modifier // TODO(P2): add config for placeholder
) {
    val imageLoader = rememberImageLoader()
    Box(modifier = modifier) {
        var loading by remember { mutableStateOf(true) }
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageLoader = imageLoader,
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onState = { state ->
                loading = when (state) {
                    AsyncImagePainter.State.Empty,
                    is AsyncImagePainter.State.Error,
                    is AsyncImagePainter.State.Loading -> true

                    is AsyncImagePainter.State.Success -> false
                }
            }
        )

        if (loading) {
            ShimmerPlaceholder(modifierForPlaceholder)
        }
    }
}

@Composable
@Suppress("MagicNumber")
fun ShimmerPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -600f, // TODO(P1): get size container
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing)
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(color = 0xFFD9D9D9),
            Color(color = 0xFFD9D9D9),
            Color(color = 0xFFF5F5F5),
            Color(color = 0xFFD9D9D9),
            Color(color = 0xFFD9D9D9),
        ),
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 600f, translateAnim + 600f),
    )

    Spacer(
        modifier = Modifier
            .background(brush = brush).then(modifier).fillMaxSize()
    )
}
