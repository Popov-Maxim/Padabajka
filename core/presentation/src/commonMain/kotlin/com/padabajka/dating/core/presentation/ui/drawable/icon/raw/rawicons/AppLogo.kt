package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
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
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.AppLogo: ImageVector
    get() {
        if (_appLogo != null) {
            return _appLogo!!
        }
        _appLogo = Builder(name = "AppLogo", defaultWidth = 201.0.dp, defaultHeight = 170.0.dp,
                viewportWidth = 201.0f, viewportHeight = 170.0f).apply {
            group {
                path(fill = linearGradient(0.0f to Color(0xFF890F74), 1.0f to Color(0xFF4C2383),
                        start = Offset(26.42f,118.66f), end = Offset(127.42f,154.85f)), stroke =
                        null, strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero) {
                    moveTo(-1.13f, 73.34f)
                    lineToRelative(151.85f, 61.72f)
                    lineToRelative(-20.04f, 49.3f)
                    lineToRelative(-151.85f, -61.72f)
                    close()
                }
                path(fill = linearGradient(0.0f to Color(0xFFB61B76), 1.0f to Color(0xFF445DB5),
                        start = Offset(-4.42f,71.28f), end = Offset(152.7f,120.66f)), stroke = null,
                        strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero) {
                    moveTo(46.53f, 111.95f)
                    curveTo(114.12f, 154.9f, 156.47f, 137.87f, 156.47f, 137.87f)
                    lineTo(164.96f, 111.95f)
                    curveTo(164.96f, 111.95f, 145.3f, 113.74f, 95.24f, 90.5f)
                    curveTo(45.19f, 67.26f, -15.57f, 15.42f, -15.57f, 15.42f)
                    lineTo(-15.57f, 37.76f)
                    curveTo(-15.57f, 37.76f, -21.06f, 69.01f, 46.53f, 111.95f)
                    close()
                }
                path(fill = linearGradient(0.0f to Color(0xFFE13C66), 0.75f to Color(0xFFB489B7),
                        start = Offset(33.57f,3.35f), end = Offset(162.28f,115.08f)), stroke = null,
                        strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f, pathFillType = NonZero) {
                    moveTo(55.92f, 83.8f)
                    curveTo(122.06f, 128.93f, 186.41f, 112.4f, 186.41f, 112.4f)
                    lineTo(192.67f, 88.71f)
                    lineTo(63.07f, -14.07f)
                    lineTo(-4.42f, 9.61f)
                    curveTo(-4.42f, 9.61f, -10.23f, 38.66f, 55.92f, 83.8f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFC693B4)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(136.17f, -21.67f)
                    lineTo(233.87f, 39.95f)
                    lineTo(202.4f, 89.84f)
                    curveTo(146.13f, 93.25f, 123.22f, 77.11f, 104.7f, 28.22f)
                    lineTo(136.17f, -21.67f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFC693B4)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(136.17f, -21.67f)
                    lineTo(233.87f, 39.95f)
                    lineTo(202.4f, 89.84f)
                    curveTo(146.13f, 93.25f, 123.22f, 77.11f, 104.7f, 28.22f)
                    lineTo(136.17f, -21.67f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFE39BAF)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(119.29f, -35.97f)
                    lineTo(242.44f, 0.78f)
                    lineTo(225.57f, 57.31f)
                    curveTo(150.99f, 57.29f, 123.85f, 49.39f, 102.42f, 20.55f)
                    lineTo(119.29f, -35.97f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFE39BAF)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(119.29f, -35.97f)
                    lineTo(242.44f, 0.78f)
                    lineTo(225.57f, 57.31f)
                    curveTo(150.99f, 57.29f, 123.85f, 49.39f, 102.42f, 20.55f)
                    lineTo(119.29f, -35.97f)
                    close()
                }
            }
        }
        .build()
        return _appLogo!!
    }

private var _appLogo: ImageVector? = null
