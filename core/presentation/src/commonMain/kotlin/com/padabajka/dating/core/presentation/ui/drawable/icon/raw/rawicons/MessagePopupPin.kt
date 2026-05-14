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

public val RawIcons.MessagePopupPin: ImageVector
    get() {
        if (_messagePopupPin != null) {
            return _messagePopupPin!!
        }
        _messagePopupPin = Builder(name = "MessagePopupPin", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.126f, 13.846f)
                lineTo(14.942f, 12.81f)
                lineTo(19.432f, 8.319f)
                lineTo(16.581f, 5.467f)
                lineTo(12.089f, 9.957f)
                lineTo(11.054f, 9.773f)
                curveTo(10.135f, 9.608f, 9.189f, 9.701f, 8.32f, 10.042f)
                lineTo(14.858f, 16.579f)
                curveTo(15.198f, 15.71f, 15.291f, 14.764f, 15.126f, 13.846f)
                verticalLineTo(13.846f)
                close()
                moveTo(10.876f, 15.45f)
                lineTo(5.17f, 21.154f)
                curveTo(4.981f, 21.343f, 4.724f, 21.449f, 4.457f, 21.449f)
                curveTo(4.19f, 21.449f, 3.933f, 21.343f, 3.744f, 21.154f)
                curveTo(3.555f, 20.965f, 3.449f, 20.708f, 3.449f, 20.441f)
                curveTo(3.449f, 20.174f, 3.555f, 19.917f, 3.744f, 19.728f)
                lineTo(9.449f, 14.024f)
                lineTo(5.17f, 9.745f)
                curveTo(5.974f, 8.94f, 6.962f, 8.341f, 8.047f, 8.0f)
                curveTo(9.133f, 7.659f, 10.286f, 7.586f, 11.406f, 7.787f)
                lineTo(15.153f, 4.04f)
                curveTo(15.34f, 3.853f, 15.563f, 3.704f, 15.807f, 3.603f)
                curveTo(16.052f, 3.501f, 16.315f, 3.449f, 16.58f, 3.449f)
                curveTo(16.844f, 3.449f, 17.107f, 3.501f, 17.352f, 3.603f)
                curveTo(17.596f, 3.704f, 17.819f, 3.853f, 18.006f, 4.04f)
                lineTo(20.858f, 6.893f)
                curveTo(21.236f, 7.271f, 21.449f, 7.784f, 21.449f, 8.319f)
                curveTo(21.449f, 8.854f, 21.236f, 9.367f, 20.858f, 9.745f)
                lineTo(17.111f, 13.492f)
                curveTo(17.312f, 14.612f, 17.239f, 15.765f, 16.898f, 16.851f)
                curveTo(16.557f, 17.937f, 15.958f, 18.924f, 15.153f, 19.728f)
                lineTo(10.876f, 15.45f)
                close()
            }
        }
        .build()
        return _messagePopupPin!!
    }

private var _messagePopupPin: ImageVector? = null
