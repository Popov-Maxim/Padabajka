package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.SmokingLifestyle: ImageVector
    get() {
        if (_smokingLifestyle != null) {
            return _smokingLifestyle!!
        }
        _smokingLifestyle = Builder(name = "SmokingLifestyle", defaultWidth = 24.0.dp, defaultHeight
                = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(18.0f, 19.0f)
                verticalLineTo(22.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(18.0f, 16.0f)
                curveTo(18.0f, 15.0f, 17.0f, 14.0f, 15.0f, 14.0f)
                curveTo(14.698f, 14.0f, 14.355f, 14.0f, 14.0f, 14.0f)
                curveTo(12.343f, 14.0f, 11.0f, 12.657f, 11.0f, 11.0f)
                verticalLineTo(8.5f)
                curveTo(11.0f, 7.119f, 12.119f, 6.0f, 13.5f, 6.0f)
                horizontalLineTo(14.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(22.0f, 16.0f)
                curveTo(22.0f, 11.5f, 20.0f, 10.5f, 18.0f, 10.0f)
                curveTo(20.0f, 9.5f, 22.0f, 9.0f, 22.0f, 6.0f)
                curveTo(22.0f, 3.0f, 19.5f, 2.0f, 18.0f, 2.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 1.5f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(22.0f, 19.0f)
                verticalLineTo(22.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 1.5f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(13.4f, 19.0f)
                horizontalLineTo(2.6f)
                curveTo(2.269f, 19.0f, 2.0f, 19.269f, 2.0f, 19.6f)
                verticalLineTo(21.4f)
                curveTo(2.0f, 21.731f, 2.269f, 22.0f, 2.6f, 22.0f)
                horizontalLineTo(13.4f)
                curveTo(13.731f, 22.0f, 14.0f, 21.731f, 14.0f, 21.4f)
                verticalLineTo(19.6f)
                curveTo(14.0f, 19.269f, 13.731f, 19.0f, 13.4f, 19.0f)
                close()
            }
        }
        .build()
        return _smokingLifestyle!!
    }

private var _smokingLifestyle: ImageVector? = null
