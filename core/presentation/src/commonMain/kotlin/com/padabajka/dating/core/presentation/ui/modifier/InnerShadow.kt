package com.padabajka.dating.core.presentation.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.innerShadow(
    color: Color = Color.Black,
    blur: Dp = 16.dp,
    x: Dp = 0.dp,
    y: Dp = 0.dp,
    shape: Shape = RectangleShape
): Modifier = this.then(
    Modifier.drawWithContent {
        val blurPx = blur.toPx()
        val dx = x.toPx()
        val dy = y.toPx()

        val outline = shape.createOutline(size, layoutDirection, this)
        val path = when (outline) {
            is Outline.Rectangle -> Path().apply {
                addRect(outline.rect)
            }

            is Outline.Rounded -> Path().apply {
                addRoundRect(outline.roundRect)
            }

            is Outline.Generic -> outline.path
        }

        clipPath(path) {
            val gradientOffsets = listOf(
                Offset(dx, dy) to Offset(dx + blurPx, dy),
                Offset(size.width + dx, dy) to Offset(size.width + dx - blurPx, dy),

                Offset(dx, dy) to Offset(dx, dy + blurPx),
                Offset(dx, size.height + dy) to Offset(dx, size.height + dy - blurPx),
            )
            gradientOffsets.onEach { (start, end) ->
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            color.copy(alpha = 0.4f),
                            color.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        start = start,
                        end = end
                    ),
                    size = size,
                    blendMode = BlendMode.Multiply
                )
            }
        }
        drawContent()
    }
)
