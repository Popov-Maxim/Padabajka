package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.NavigationMessageIcon: ImageVector
    get() {
        if (_navigationMessageIcon != null) {
            return _navigationMessageIcon!!
        }
        _navigationMessageIcon = Builder(name = "NavigationMessageIcon", defaultWidth = 30.0.dp,
                defaultHeight = 30.0.dp, viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(26.25f, 14.375f)
                curveTo(26.254f, 16.025f, 25.869f, 17.652f, 25.125f, 19.125f)
                curveTo(24.243f, 20.89f, 22.887f, 22.374f, 21.209f, 23.412f)
                curveTo(19.531f, 24.449f, 17.598f, 24.999f, 15.625f, 25.0f)
                curveTo(13.975f, 25.004f, 12.348f, 24.619f, 10.875f, 23.875f)
                lineTo(3.75f, 26.25f)
                lineTo(6.125f, 19.125f)
                curveTo(5.381f, 17.652f, 4.996f, 16.025f, 5.0f, 14.375f)
                curveTo(5.001f, 12.402f, 5.551f, 10.469f, 6.588f, 8.791f)
                curveTo(7.626f, 7.113f, 9.11f, 5.757f, 10.875f, 4.875f)
                curveTo(12.348f, 4.131f, 13.975f, 3.746f, 15.625f, 3.75f)
                horizontalLineTo(16.25f)
                curveTo(18.855f, 3.894f, 21.316f, 4.993f, 23.161f, 6.839f)
                curveTo(25.007f, 8.684f, 26.106f, 11.144f, 26.25f, 13.75f)
                verticalLineTo(14.375f)
                close()
            }
        }
        .build()
        return _navigationMessageIcon!!
    }

private var _navigationMessageIcon: ImageVector? = null
