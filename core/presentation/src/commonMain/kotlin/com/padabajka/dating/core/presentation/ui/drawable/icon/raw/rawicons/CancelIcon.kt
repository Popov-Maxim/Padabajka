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

public val RawIcons.CancelIcon: ImageVector
    get() {
        if (_cancelIcon != null) {
            return _cancelIcon!!
        }
        _cancelIcon = Builder(name = "CancelIcon", defaultWidth = 30.0.dp, defaultHeight = 30.0.dp,
                viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(23.408f, 0.982f)
                curveTo(24.718f, -0.327f, 26.842f, -0.328f, 28.152f, 0.982f)
                curveTo(29.462f, 2.293f, 29.462f, 4.417f, 28.152f, 5.727f)
                lineTo(19.524f, 14.355f)
                lineTo(28.578f, 23.408f)
                curveTo(29.888f, 24.718f, 29.888f, 26.842f, 28.578f, 28.152f)
                curveTo(27.268f, 29.462f, 25.144f, 29.462f, 23.834f, 28.152f)
                lineTo(14.78f, 19.099f)
                lineTo(5.727f, 28.152f)
                curveTo(4.417f, 29.462f, 2.293f, 29.462f, 0.982f, 28.152f)
                curveTo(-0.328f, 26.842f, -0.327f, 24.718f, 0.982f, 23.408f)
                lineTo(10.036f, 14.355f)
                lineTo(1.408f, 5.727f)
                curveTo(0.098f, 4.417f, 0.098f, 2.293f, 1.408f, 0.982f)
                curveTo(2.718f, -0.327f, 4.842f, -0.328f, 6.152f, 0.982f)
                lineTo(14.78f, 9.61f)
                lineTo(23.408f, 0.982f)
                close()
            }
        }
        .build()
        return _cancelIcon!!
    }

private var _cancelIcon: ImageVector? = null
