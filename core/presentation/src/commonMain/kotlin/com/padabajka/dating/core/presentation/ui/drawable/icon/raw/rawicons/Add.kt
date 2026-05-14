package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.Add: ImageVector
    get() {
        if (_add != null) {
            return _add!!
        }
        _add = Builder(name = "Add", defaultWidth = 18.0.dp, defaultHeight = 18.0.dp, viewportWidth
                = 18.0f, viewportHeight = 18.0f).apply {
            path(fill = SolidColor(Color(0xFF6EA3CA)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(13.5f, 9.563f)
                horizontalLineTo(4.5f)
                curveTo(4.193f, 9.563f, 3.938f, 9.307f, 3.938f, 9.0f)
                curveTo(3.938f, 8.693f, 4.193f, 8.438f, 4.5f, 8.438f)
                horizontalLineTo(13.5f)
                curveTo(13.807f, 8.438f, 14.063f, 8.693f, 14.063f, 9.0f)
                curveTo(14.063f, 9.307f, 13.807f, 9.563f, 13.5f, 9.563f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6EA3CA)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(9.0f, 14.063f)
                curveTo(8.693f, 14.063f, 8.438f, 13.807f, 8.438f, 13.5f)
                verticalLineTo(4.5f)
                curveTo(8.438f, 4.193f, 8.693f, 3.938f, 9.0f, 3.938f)
                curveTo(9.307f, 3.938f, 9.563f, 4.193f, 9.563f, 4.5f)
                verticalLineTo(13.5f)
                curveTo(9.563f, 13.807f, 9.307f, 14.063f, 9.0f, 14.063f)
                close()
            }
        }
        .build()
        return _add!!
    }

private var _add: ImageVector? = null
