package com.padabajka.dating.feature.profile.presentation.editor.asset.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TriangleArrow(
    direction: Direction = Direction.Up,
    color: Color = Color.Black,
    size: Dp = 12.dp
) {
    val sizePx = with(LocalDensity.current) { size.toPx() }

    Canvas(modifier = Modifier.size(size)) {
        val path = Path()

        when (direction) {
            Direction.Up -> {
                path.moveTo(sizePx / 2f, 0f)
                path.lineTo(0f, sizePx)
                path.lineTo(sizePx, sizePx)
            }

            Direction.Down -> {
                path.moveTo(0f, 0f)
                path.lineTo(sizePx, 0f)
                path.lineTo(sizePx / 2f, sizePx)
            }

            Direction.Left -> {
                path.moveTo(sizePx, 0f)
                path.lineTo(0f, sizePx / 2f)
                path.lineTo(sizePx, sizePx)
            }

            Direction.Right -> {
                path.moveTo(0f, 0f)
                path.lineTo(sizePx, sizePx / 2f)
                path.lineTo(0f, sizePx)
            }
        }

        path.close()
        drawPath(path = path, color = color)
    }
}

enum class Direction { Up, Down, Left, Right }
