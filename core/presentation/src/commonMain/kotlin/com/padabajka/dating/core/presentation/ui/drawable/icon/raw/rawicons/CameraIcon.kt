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

public val RawIcons.CameraIcon: ImageVector
    get() {
        if (_cameraIcon != null) {
            return _cameraIcon!!
        }
        _cameraIcon = Builder(name = "CameraIcon", defaultWidth = 46.0.dp, defaultHeight = 46.0.dp,
                viewportWidth = 46.0f, viewportHeight = 46.0f).apply {
            path(fill = SolidColor(Color(0xFFFFFBFB)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(26.381f, 5.75f)
                curveTo(28.559f, 5.75f, 30.55f, 6.981f, 31.524f, 8.929f)
                lineTo(33.238f, 12.357f)
                curveTo(33.563f, 13.007f, 34.227f, 13.417f, 34.952f, 13.417f)
                horizontalLineTo(36.417f)
                curveTo(39.592f, 13.417f, 42.167f, 15.991f, 42.167f, 19.167f)
                verticalLineTo(34.5f)
                curveTo(42.167f, 37.676f, 39.592f, 40.25f, 36.417f, 40.25f)
                horizontalLineTo(9.583f)
                curveTo(6.408f, 40.25f, 3.833f, 37.676f, 3.833f, 34.5f)
                verticalLineTo(19.167f)
                curveTo(3.833f, 15.991f, 6.408f, 13.417f, 9.583f, 13.417f)
                horizontalLineTo(11.048f)
                curveTo(11.774f, 13.417f, 12.437f, 13.007f, 12.762f, 12.357f)
                lineTo(14.476f, 8.929f)
                curveTo(15.45f, 6.981f, 17.441f, 5.75f, 19.619f, 5.75f)
                horizontalLineTo(26.381f)
                close()
                moveTo(26.381f, 9.583f)
                horizontalLineTo(19.619f)
                curveTo(18.893f, 9.583f, 18.229f, 9.993f, 17.905f, 10.643f)
                lineTo(16.191f, 14.071f)
                curveTo(15.217f, 16.02f, 13.226f, 17.25f, 11.048f, 17.25f)
                horizontalLineTo(9.583f)
                curveTo(8.525f, 17.25f, 7.667f, 18.108f, 7.667f, 19.167f)
                verticalLineTo(34.5f)
                curveTo(7.667f, 35.558f, 8.525f, 36.417f, 9.583f, 36.417f)
                horizontalLineTo(36.417f)
                curveTo(37.475f, 36.417f, 38.333f, 35.558f, 38.333f, 34.5f)
                verticalLineTo(19.167f)
                curveTo(38.333f, 18.108f, 37.475f, 17.25f, 36.417f, 17.25f)
                horizontalLineTo(34.952f)
                curveTo(32.775f, 17.25f, 30.784f, 16.02f, 29.809f, 14.071f)
                lineTo(28.095f, 10.643f)
                curveTo(27.771f, 9.993f, 27.107f, 9.583f, 26.381f, 9.583f)
                close()
                moveTo(23.0f, 17.25f)
                curveTo(27.234f, 17.25f, 30.667f, 20.683f, 30.667f, 24.917f)
                curveTo(30.667f, 29.151f, 27.234f, 32.583f, 23.0f, 32.583f)
                curveTo(18.766f, 32.583f, 15.333f, 29.151f, 15.333f, 24.917f)
                curveTo(15.333f, 20.683f, 18.766f, 17.25f, 23.0f, 17.25f)
                close()
                moveTo(23.0f, 21.083f)
                curveTo(20.883f, 21.083f, 19.167f, 22.8f, 19.167f, 24.917f)
                curveTo(19.167f, 27.034f, 20.883f, 28.75f, 23.0f, 28.75f)
                curveTo(25.117f, 28.75f, 26.833f, 27.034f, 26.833f, 24.917f)
                curveTo(26.833f, 22.8f, 25.117f, 21.083f, 23.0f, 21.083f)
                close()
            }
        }
        .build()
        return _cameraIcon!!
    }

private var _cameraIcon: ImageVector? = null
