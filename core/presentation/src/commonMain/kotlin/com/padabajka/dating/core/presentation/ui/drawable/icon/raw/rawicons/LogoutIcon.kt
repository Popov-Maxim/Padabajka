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

public val RawIcons.LogoutIcon: ImageVector
    get() {
        if (_logoutIcon != null) {
            return _logoutIcon!!
        }
        _logoutIcon = Builder(name = "LogoutIcon", defaultWidth = 30.0.dp, defaultHeight = 30.0.dp,
                viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(19.05f, 27.837f)
                horizontalLineTo(18.887f)
                curveTo(13.337f, 27.837f, 10.662f, 25.65f, 10.2f, 20.75f)
                curveTo(10.15f, 20.237f, 10.525f, 19.775f, 11.05f, 19.725f)
                curveTo(11.55f, 19.675f, 12.025f, 20.063f, 12.075f, 20.575f)
                curveTo(12.437f, 24.5f, 14.287f, 25.962f, 18.9f, 25.962f)
                horizontalLineTo(19.062f)
                curveTo(24.15f, 25.962f, 25.95f, 24.163f, 25.95f, 19.075f)
                verticalLineTo(10.925f)
                curveTo(25.95f, 5.837f, 24.15f, 4.037f, 19.062f, 4.037f)
                horizontalLineTo(18.9f)
                curveTo(14.262f, 4.037f, 12.412f, 5.525f, 12.075f, 9.525f)
                curveTo(12.012f, 10.038f, 11.575f, 10.425f, 11.05f, 10.375f)
                curveTo(10.525f, 10.337f, 10.15f, 9.875f, 10.187f, 9.362f)
                curveTo(10.612f, 4.387f, 13.3f, 2.162f, 18.887f, 2.162f)
                horizontalLineTo(19.05f)
                curveTo(25.187f, 2.162f, 27.812f, 4.787f, 27.812f, 10.925f)
                verticalLineTo(19.075f)
                curveTo(27.812f, 25.212f, 25.187f, 27.837f, 19.05f, 27.837f)
                close()
            }
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(18.75f, 15.938f)
                horizontalLineTo(4.525f)
                curveTo(4.012f, 15.938f, 3.587f, 15.512f, 3.587f, 15.0f)
                curveTo(3.587f, 14.488f, 4.012f, 14.063f, 4.525f, 14.063f)
                horizontalLineTo(18.75f)
                curveTo(19.262f, 14.063f, 19.687f, 14.488f, 19.687f, 15.0f)
                curveTo(19.687f, 15.512f, 19.262f, 15.938f, 18.75f, 15.938f)
                close()
            }
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(7.313f, 20.125f)
                curveTo(7.075f, 20.125f, 6.838f, 20.038f, 6.65f, 19.85f)
                lineTo(2.463f, 15.663f)
                curveTo(2.1f, 15.3f, 2.1f, 14.7f, 2.463f, 14.337f)
                lineTo(6.65f, 10.15f)
                curveTo(7.013f, 9.788f, 7.613f, 9.788f, 7.975f, 10.15f)
                curveTo(8.338f, 10.512f, 8.338f, 11.113f, 7.975f, 11.475f)
                lineTo(4.45f, 15.0f)
                lineTo(7.975f, 18.525f)
                curveTo(8.338f, 18.888f, 8.338f, 19.487f, 7.975f, 19.85f)
                curveTo(7.8f, 20.038f, 7.55f, 20.125f, 7.313f, 20.125f)
                close()
            }
        }
        .build()
        return _logoutIcon!!
    }

private var _logoutIcon: ImageVector? = null
