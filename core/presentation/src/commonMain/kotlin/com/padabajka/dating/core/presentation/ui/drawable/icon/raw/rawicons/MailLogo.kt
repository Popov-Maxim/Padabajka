package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

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
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.MailLogo: ImageVector
    get() {
        if (_mailLogo != null) {
            return _mailLogo!!
        }
        _mailLogo = Builder(name = "MailLogo", defaultWidth = 20.0.dp, defaultHeight = 20.0.dp,
                viewportWidth = 20.0f, viewportHeight = 20.0f).apply {
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(14.167f, 17.708f)
                horizontalLineTo(5.833f)
                curveTo(2.792f, 17.708f, 1.042f, 15.958f, 1.042f, 12.917f)
                verticalLineTo(7.083f)
                curveTo(1.042f, 4.042f, 2.792f, 2.292f, 5.833f, 2.292f)
                horizontalLineTo(14.167f)
                curveTo(17.208f, 2.292f, 18.958f, 4.042f, 18.958f, 7.083f)
                verticalLineTo(12.917f)
                curveTo(18.958f, 15.958f, 17.208f, 17.708f, 14.167f, 17.708f)
                close()
                moveTo(5.833f, 3.542f)
                curveTo(3.45f, 3.542f, 2.292f, 4.7f, 2.292f, 7.083f)
                verticalLineTo(12.917f)
                curveTo(2.292f, 15.3f, 3.45f, 16.458f, 5.833f, 16.458f)
                horizontalLineTo(14.167f)
                curveTo(16.55f, 16.458f, 17.708f, 15.3f, 17.708f, 12.917f)
                verticalLineTo(7.083f)
                curveTo(17.708f, 4.7f, 16.55f, 3.542f, 14.167f, 3.542f)
                horizontalLineTo(5.833f)
                close()
            }
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(10.0f, 10.725f)
                curveTo(9.3f, 10.725f, 8.592f, 10.508f, 8.05f, 10.067f)
                lineTo(5.442f, 7.983f)
                curveTo(5.175f, 7.767f, 5.125f, 7.375f, 5.342f, 7.108f)
                curveTo(5.558f, 6.842f, 5.95f, 6.792f, 6.217f, 7.008f)
                lineTo(8.825f, 9.092f)
                curveTo(9.458f, 9.6f, 10.533f, 9.6f, 11.167f, 9.092f)
                lineTo(13.775f, 7.008f)
                curveTo(14.042f, 6.792f, 14.441f, 6.833f, 14.65f, 7.108f)
                curveTo(14.866f, 7.375f, 14.825f, 7.775f, 14.55f, 7.983f)
                lineTo(11.941f, 10.067f)
                curveTo(11.408f, 10.508f, 10.7f, 10.725f, 10.0f, 10.725f)
                close()
            }
        }
        .build()
        return _mailLogo!!
    }

private var _mailLogo: ImageVector? = null
