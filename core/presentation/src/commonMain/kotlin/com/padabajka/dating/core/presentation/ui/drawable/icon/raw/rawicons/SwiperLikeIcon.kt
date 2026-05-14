package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.SwiperLikeIcon: ImageVector
    get() {
        if (_swiperLikeIcon != null) {
            return _swiperLikeIcon!!
        }
        _swiperLikeIcon = Builder(name = "SwiperLikeIcon", defaultWidth = 40.0.dp, defaultHeight =
                33.0.dp, viewportWidth = 40.0f, viewportHeight = 33.0f).apply {
            path(fill = linearGradient(0.0f to Color(0xFFA6EA21), 1.0f to Color(0xFF47C04C), start =
                    Offset(6.225f,0.0f), end = Offset(32.912f,33.005f)), stroke = null,
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(19.206f, 14.331f)
                curveTo(19.366f, 13.929f, 19.917f, 13.849f, 20.169f, 14.201f)
                curveTo(21.293f, 15.774f, 22.525f, 17.363f, 24.154f, 18.75f)
                curveTo(26.057f, 20.37f, 28.483f, 21.704f, 31.895f, 22.529f)
                lineTo(21.024f, 32.486f)
                curveTo(20.627f, 32.849f, 20.119f, 33.016f, 19.621f, 32.993f)
                curveTo(19.091f, 33.046f, 18.541f, 32.884f, 18.116f, 32.495f)
                lineTo(10.453f, 25.477f)
                curveTo(15.549f, 20.91f, 17.989f, 17.395f, 19.206f, 14.331f)
                close()
                moveTo(2.309f, 3.777f)
                curveTo(7.187f, 4.021f, 10.465f, 5.023f, 12.861f, 6.406f)
                curveTo(15.091f, 7.691f, 16.612f, 9.336f, 17.975f, 11.134f)
                curveTo(18.067f, 11.255f, 18.101f, 11.412f, 18.066f, 11.56f)
                curveTo(18.001f, 11.833f, 17.928f, 12.111f, 17.844f, 12.393f)
                curveTo(16.964f, 15.351f, 14.862f, 18.973f, 9.029f, 24.172f)
                lineTo(3.796f, 19.379f)
                curveTo(-0.67f, 15.289f, -1.234f, 8.53f, 2.309f, 3.777f)
                close()
                moveTo(23.678f, 2.303f)
                curveTo(27.25f, -0.969f, 32.798f, -0.725f, 36.069f, 2.847f)
                curveTo(40.431f, 7.61f, 40.107f, 15.007f, 35.345f, 19.369f)
                lineTo(33.314f, 21.229f)
                curveTo(29.542f, 20.486f, 27.053f, 19.151f, 25.175f, 17.552f)
                curveTo(23.354f, 16.001f, 22.085f, 14.194f, 20.753f, 12.298f)
                lineTo(20.211f, 11.529f)
                curveTo(20.128f, 11.411f, 20.096f, 11.264f, 20.122f, 11.122f)
                curveTo(20.325f, 10.022f, 20.417f, 8.988f, 20.506f, 8.005f)
                curveTo(20.513f, 7.933f, 20.519f, 7.86f, 20.525f, 7.788f)
                curveTo(20.618f, 6.774f, 20.715f, 5.81f, 20.916f, 4.831f)
                lineTo(23.678f, 2.303f)
                close()
                moveTo(3.642f, 2.288f)
                curveTo(6.958f, -0.731f, 12.091f, -0.776f, 15.463f, 2.313f)
                lineTo(18.857f, 5.421f)
                curveTo(18.743f, 6.174f, 18.674f, 6.903f, 18.61f, 7.614f)
                lineTo(18.594f, 7.79f)
                curveTo(18.588f, 7.851f, 18.583f, 7.911f, 18.577f, 7.971f)
                curveTo(18.535f, 8.432f, 17.957f, 8.61f, 17.643f, 8.27f)
                curveTo(16.553f, 7.088f, 15.273f, 5.98f, 13.648f, 5.042f)
                curveTo(11.208f, 3.635f, 8.039f, 2.642f, 3.642f, 2.288f)
                close()
            }
        }
        .build()
        return _swiperLikeIcon!!
    }

private var _swiperLikeIcon: ImageVector? = null
