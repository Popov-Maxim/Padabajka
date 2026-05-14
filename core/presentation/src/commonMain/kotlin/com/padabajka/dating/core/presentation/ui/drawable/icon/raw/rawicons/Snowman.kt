package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.Snowman: ImageVector
    get() {
        if (_snowman != null) {
            return _snowman!!
        }
        _snowman = Builder(name = "Snowman", defaultWidth = 30.0.dp, defaultHeight = 30.0.dp,
                viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            group {
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                        strokeLineWidth = 2.5f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(15.0f, 3.75f)
                    curveTo(15.977f, 3.75f, 16.933f, 4.036f, 17.75f, 4.573f)
                    curveTo(18.566f, 5.11f, 19.208f, 5.875f, 19.594f, 6.773f)
                    curveTo(19.98f, 7.671f, 20.095f, 8.662f, 19.924f, 9.624f)
                    curveTo(19.754f, 10.587f, 19.304f, 11.478f, 18.632f, 12.188f)
                    curveTo(20.087f, 12.993f, 21.233f, 14.258f, 21.89f, 15.785f)
                    curveTo(22.548f, 17.313f, 22.679f, 19.015f, 22.263f, 20.625f)
                    curveTo(21.847f, 22.235f, 20.908f, 23.661f, 19.594f, 24.679f)
                    curveTo(18.279f, 25.697f, 16.664f, 26.25f, 15.001f, 26.25f)
                    curveTo(13.339f, 26.25f, 11.723f, 25.697f, 10.408f, 24.679f)
                    curveTo(9.094f, 23.661f, 8.155f, 22.235f, 7.739f, 20.625f)
                    curveTo(7.324f, 19.015f, 7.455f, 17.313f, 8.112f, 15.785f)
                    curveTo(8.769f, 14.258f, 9.915f, 12.993f, 11.37f, 12.188f)
                    curveTo(10.698f, 11.478f, 10.249f, 10.587f, 10.078f, 9.625f)
                    curveTo(9.907f, 8.663f, 10.022f, 7.672f, 10.408f, 6.774f)
                    curveTo(10.794f, 5.876f, 11.435f, 5.112f, 12.251f, 4.574f)
                    curveTo(13.067f, 4.037f, 14.023f, 3.75f, 15.0f, 3.75f)
                    close()
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                        strokeLineWidth = 2.5f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(21.875f, 14.375f)
                    lineTo(25.0f, 12.5f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                        strokeLineWidth = 2.5f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(8.125f, 14.375f)
                    lineTo(5.0f, 12.5f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                        strokeLineWidth = 2.5f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(15.0f, 16.25f)
                    horizontalLineTo(15.012f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                        strokeLineWidth = 2.5f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(15.0f, 20.0f)
                    horizontalLineTo(15.012f)
                }
            }
        }
        .build()
        return _snowman!!
    }

private var _snowman: ImageVector? = null
