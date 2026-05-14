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

public val RawIcons.MessagePopupTrash: ImageVector
    get() {
        if (_messagePopupTrash != null) {
            return _messagePopupTrash!!
        }
        _messagePopupTrash = Builder(name = "MessagePopupTrash", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF21272A)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(19.0f, 7.0f)
                lineTo(18.133f, 19.142f)
                curveTo(18.058f, 20.189f, 17.187f, 21.0f, 16.138f, 21.0f)
                horizontalLineTo(7.862f)
                curveTo(6.813f, 21.0f, 5.942f, 20.189f, 5.867f, 19.142f)
                lineTo(5.0f, 7.0f)
                moveTo(10.0f, 11.0f)
                verticalLineTo(17.0f)
                moveTo(14.0f, 11.0f)
                verticalLineTo(17.0f)
                moveTo(15.0f, 7.0f)
                verticalLineTo(4.0f)
                curveTo(15.0f, 3.448f, 14.552f, 3.0f, 14.0f, 3.0f)
                horizontalLineTo(10.0f)
                curveTo(9.448f, 3.0f, 9.0f, 3.448f, 9.0f, 4.0f)
                verticalLineTo(7.0f)
                moveTo(4.0f, 7.0f)
                horizontalLineTo(20.0f)
            }
        }
        .build()
        return _messagePopupTrash!!
    }

private var _messagePopupTrash: ImageVector? = null
