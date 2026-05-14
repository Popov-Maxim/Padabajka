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
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.RewindIcon: ImageVector
    get() {
        if (_rewindIcon != null) {
            return _rewindIcon!!
        }
        _rewindIcon = Builder(name = "RewindIcon", defaultWidth = 22.0.dp, defaultHeight = 20.0.dp,
                viewportWidth = 22.0f, viewportHeight = 20.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                    strokeLineWidth = 4.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(5.5f, 2.0f)
                lineTo(2.0f, 5.0f)
                lineTo(5.5f, 8.5f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF333333)),
                    strokeLineWidth = 4.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(2.0f, 5.0f)
                horizontalLineTo(13.497f)
                curveTo(16.938f, 5.0f, 19.861f, 7.81f, 19.995f, 11.25f)
                curveTo(20.137f, 14.885f, 17.133f, 18.0f, 13.497f, 18.0f)
                horizontalLineTo(4.999f)
            }
        }
        .build()
        return _rewindIcon!!
    }

private var _rewindIcon: ImageVector? = null
