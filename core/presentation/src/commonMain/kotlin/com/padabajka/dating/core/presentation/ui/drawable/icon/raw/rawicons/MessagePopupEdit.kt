package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.MessagePopupEdit: ImageVector
    get() {
        if (_messagePopupEdit != null) {
            return _messagePopupEdit!!
        }
        _messagePopupEdit = Builder(name = "MessagePopupEdit", defaultWidth = 24.0.dp, defaultHeight
                = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(14.709f, 3.685f)
                curveTo(16.369f, 2.105f, 19.06f, 2.105f, 20.719f, 3.685f)
                curveTo(22.379f, 5.264f, 22.379f, 7.825f, 20.719f, 9.405f)
                lineTo(9.439f, 20.14f)
                horizontalLineTo(21.286f)
                curveTo(21.68f, 20.14f, 22.0f, 20.444f, 22.0f, 20.82f)
                curveTo(22.0f, 21.195f, 21.68f, 21.5f, 21.286f, 21.5f)
                horizontalLineTo(2.714f)
                curveTo(2.32f, 21.5f, 2.0f, 21.195f, 2.0f, 20.82f)
                verticalLineTo(16.062f)
                curveTo(2.0f, 15.882f, 2.075f, 15.708f, 2.209f, 15.581f)
                lineTo(14.709f, 3.685f)
                close()
                moveTo(7.419f, 20.14f)
                lineTo(17.061f, 10.963f)
                lineTo(13.071f, 7.166f)
                lineTo(3.429f, 16.343f)
                verticalLineTo(20.14f)
                horizontalLineTo(7.419f)
                close()
                moveTo(14.081f, 6.205f)
                lineTo(18.071f, 10.002f)
                lineTo(19.709f, 8.443f)
                curveTo(20.811f, 7.394f, 20.811f, 5.694f, 19.709f, 4.646f)
                curveTo(18.607f, 3.597f, 16.821f, 3.597f, 15.719f, 4.646f)
                lineTo(14.081f, 6.205f)
                close()
            }
        }
        .build()
        return _messagePopupEdit!!
    }

private var _messagePopupEdit: ImageVector? = null
