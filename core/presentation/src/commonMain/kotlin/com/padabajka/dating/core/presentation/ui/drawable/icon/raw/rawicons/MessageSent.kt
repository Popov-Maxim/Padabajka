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

public val RawIcons.MessageSent: ImageVector
    get() {
        if (_messageSent != null) {
            return _messageSent!!
        }
        _messageSent = Builder(name = "MessageSent", defaultWidth = 56.0.dp, defaultHeight =
                56.0.dp, viewportWidth = 56.0f, viewportHeight = 56.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(22.225f, 41.2f)
                lineTo(10.675f, 29.65f)
                lineTo(13.974f, 26.35f)
                lineTo(22.229f, 34.595f)
                lineTo(22.225f, 34.599f)
                lineTo(42.023f, 14.8f)
                lineTo(45.323f, 18.1f)
                lineTo(25.524f, 37.9f)
                lineTo(22.227f, 41.197f)
                lineTo(22.225f, 41.2f)
                close()
            }
        }
        .build()
        return _messageSent!!
    }

private var _messageSent: ImageVector? = null
