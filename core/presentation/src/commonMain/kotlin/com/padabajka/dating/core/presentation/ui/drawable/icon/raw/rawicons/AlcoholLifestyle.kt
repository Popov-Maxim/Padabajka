package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.AlcoholLifestyle: ImageVector
    get() {
        if (_alcoholLifestyle != null) {
            return _alcoholLifestyle!!
        }
        _alcoholLifestyle = Builder(name = "AlcoholLifestyle", defaultWidth = 24.0.dp, defaultHeight
                = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            group {
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                        strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(17.9f, 6.5f)
                    horizontalLineTo(16.0f)
                    lineTo(10.5f, 16.0f)
                    lineTo(4.9f, 6.5f)
                    horizontalLineTo(3.0f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                        strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero) {
                    moveTo(12.875f, 12.798f)
                    curveTo(15.676f, 14.373f, 19.223f, 13.38f, 20.798f, 10.58f)
                    curveTo(22.374f, 7.779f, 21.38f, 4.232f, 18.58f, 2.657f)
                    curveTo(15.78f, 1.082f, 12.232f, 2.075f, 10.657f, 4.875f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                        strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(13.0f, 22.0f)
                    horizontalLineTo(8.0f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                        strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(10.5f, 22.0f)
                    verticalLineTo(16.0f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                        strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(6.0f, 8.0f)
                    curveTo(6.0f, 8.0f, 7.0f, 7.0f, 8.5f, 7.0f)
                    curveTo(10.0f, 7.0f, 11.0f, 8.5f, 12.5f, 8.5f)
                    curveTo(14.0f, 8.5f, 15.0f, 8.0f, 15.0f, 8.0f)
                }
            }
        }
        .build()
        return _alcoholLifestyle!!
    }

private var _alcoholLifestyle: ImageVector? = null
