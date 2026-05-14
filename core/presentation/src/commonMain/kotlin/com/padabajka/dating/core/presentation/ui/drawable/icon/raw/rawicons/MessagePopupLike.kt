package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.MessagePopupLike: ImageVector
    get() {
        if (_messagePopupLike != null) {
            return _messagePopupLike!!
        }
        _messagePopupLike = Builder(name = "MessagePopupLike", defaultWidth = 24.0.dp, defaultHeight
                = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Round,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(12.0f, 21.0f)
                lineTo(10.55f, 19.705f)
                curveTo(5.4f, 15.124f, 2.0f, 12.103f, 2.0f, 8.395f)
                curveTo(2.0f, 5.374f, 4.42f, 3.0f, 7.5f, 3.0f)
                curveTo(9.24f, 3.0f, 10.91f, 3.795f, 12.0f, 5.05f)
                curveTo(13.09f, 3.795f, 14.76f, 3.0f, 16.5f, 3.0f)
                curveTo(19.58f, 3.0f, 22.0f, 5.374f, 22.0f, 8.395f)
                curveTo(22.0f, 12.103f, 18.6f, 15.124f, 13.45f, 19.715f)
                lineTo(12.0f, 21.0f)
                close()
            }
        }
        .build()
        return _messagePopupLike!!
    }

private var _messagePopupLike: ImageVector? = null
