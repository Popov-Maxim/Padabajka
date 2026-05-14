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

public val RawIcons.SwiperSuperLikeIcon: ImageVector
    get() {
        if (_swiperSuperLikeIcon != null) {
            return _swiperSuperLikeIcon!!
        }
        _swiperSuperLikeIcon = Builder(name = "SwiperSuperLikeIcon", defaultWidth = 23.0.dp,
                defaultHeight = 22.0.dp, viewportWidth = 23.0f, viewportHeight = 22.0f).apply {
            path(fill = linearGradient(0.0f to Color(0xFFBE4FEB), 1.0f to Color(0xFF6C2D85), start =
                    Offset(1.933f,1.977f), end = Offset(20.529f,17.928f)), stroke = null,
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(9.334f, 1.365f)
                curveTo(9.944f, -0.455f, 12.518f, -0.455f, 13.127f, 1.365f)
                lineTo(14.446f, 5.302f)
                curveTo(14.717f, 6.111f, 15.471f, 6.659f, 16.324f, 6.667f)
                lineTo(20.476f, 6.704f)
                curveTo(22.395f, 6.721f, 23.191f, 9.169f, 21.648f, 10.311f)
                lineTo(18.311f, 12.782f)
                curveTo(17.626f, 13.29f, 17.338f, 14.177f, 17.594f, 14.99f)
                lineTo(18.841f, 18.951f)
                curveTo(19.418f, 20.781f, 17.336f, 22.294f, 15.773f, 21.18f)
                lineTo(12.392f, 18.77f)
                curveTo(11.697f, 18.274f, 10.765f, 18.274f, 10.07f, 18.77f)
                lineTo(6.689f, 21.18f)
                curveTo(5.126f, 22.294f, 3.044f, 20.781f, 3.621f, 18.951f)
                lineTo(4.868f, 14.99f)
                curveTo(5.124f, 14.177f, 4.836f, 13.29f, 4.15f, 12.782f)
                lineTo(0.814f, 10.311f)
                curveTo(-0.729f, 9.169f, 0.067f, 6.721f, 1.986f, 6.704f)
                lineTo(6.137f, 6.667f)
                curveTo(6.991f, 6.659f, 7.745f, 6.111f, 8.016f, 5.302f)
                lineTo(9.334f, 1.365f)
                close()
            }
        }
        .build()
        return _swiperSuperLikeIcon!!
    }

private var _swiperSuperLikeIcon: ImageVector? = null
