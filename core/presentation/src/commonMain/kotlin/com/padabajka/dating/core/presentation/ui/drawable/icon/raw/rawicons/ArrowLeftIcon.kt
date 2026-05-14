package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.ArrowLeftIcon: ImageVector
    get() {
        if (_arrowLeftIcon != null) {
            return _arrowLeftIcon!!
        }
        _arrowLeftIcon = Builder(name = "ArrowLeftIcon", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.0f, 20.67f)
                curveTo(14.81f, 20.67f, 14.62f, 20.6f, 14.47f, 20.45f)
                lineTo(7.95f, 13.93f)
                curveTo(6.89f, 12.87f, 6.89f, 11.13f, 7.95f, 10.07f)
                lineTo(14.47f, 3.55f)
                curveTo(14.76f, 3.26f, 15.24f, 3.26f, 15.53f, 3.55f)
                curveTo(15.82f, 3.84f, 15.82f, 4.32f, 15.53f, 4.61f)
                lineTo(9.01f, 11.13f)
                curveTo(8.53f, 11.61f, 8.53f, 12.39f, 9.01f, 12.87f)
                lineTo(15.53f, 19.39f)
                curveTo(15.82f, 19.68f, 15.82f, 20.16f, 15.53f, 20.45f)
                curveTo(15.38f, 20.59f, 15.19f, 20.67f, 15.0f, 20.67f)
                close()
            }
        }
        .build()
        return _arrowLeftIcon!!
    }

private var _arrowLeftIcon: ImageVector? = null
