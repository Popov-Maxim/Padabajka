package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.image.CoreAsyncImage
import com.padabajka.dating.core.presentation.ui.modifier.optionalClickable

@Composable
fun ProfileAvatar(
    model: Any?,
    modifier: Modifier = Modifier.size(80.dp),
    onClick: (() -> Unit)? = null
) {
    val imageShape = CircleShape
    CoreAsyncImage(
        model = model,
        modifier = modifier.background(
            color = Color.DarkGray,
            shape = imageShape
        ).clip(imageShape).optionalClickable(onClick),
    )
}
