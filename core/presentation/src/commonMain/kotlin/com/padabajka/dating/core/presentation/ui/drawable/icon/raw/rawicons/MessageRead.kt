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

public val RawIcons.MessageRead: ImageVector
    get() {
        if (_messageRead != null) {
            return _messageRead!!
        }
        _messageRead = Builder(name = "MessageRead", defaultWidth = 56.0.dp, defaultHeight =
                56.0.dp, viewportWidth = 56.0f, viewportHeight = 56.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(27.176f, 42.023f)
                lineTo(15.626f, 30.476f)
                lineTo(18.923f, 27.174f)
                lineTo(27.176f, 35.427f)
                lineTo(46.975f, 15.626f)
                lineTo(50.274f, 18.926f)
                lineTo(27.174f, 42.026f)
                lineTo(27.176f, 42.023f)
                close()
                moveTo(17.276f, 42.023f)
                lineTo(5.726f, 30.476f)
                lineTo(9.025f, 27.174f)
                lineTo(20.575f, 38.724f)
                lineTo(17.278f, 42.023f)
                horizontalLineTo(17.276f)
                close()
                moveTo(27.176f, 32.125f)
                lineTo(23.875f, 28.826f)
                lineTo(38.724f, 13.977f)
                lineTo(42.026f, 17.276f)
                lineTo(27.176f, 32.123f)
                verticalLineTo(32.125f)
                close()
            }
        }
        .build()
        return _messageRead!!
    }

private var _messageRead: ImageVector? = null
