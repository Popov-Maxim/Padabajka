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

public val RawIcons.NavigationProfileIcon: ImageVector
    get() {
        if (_navigationProfileIcon != null) {
            return _navigationProfileIcon!!
        }
        _navigationProfileIcon = Builder(name = "NavigationProfileIcon", defaultWidth = 30.0.dp,
                defaultHeight = 30.0.dp, viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.2f, 14.525f)
                curveTo(15.162f, 14.525f, 15.137f, 14.525f, 15.099f, 14.525f)
                curveTo(15.037f, 14.512f, 14.95f, 14.512f, 14.875f, 14.525f)
                curveTo(11.25f, 14.413f, 8.512f, 11.563f, 8.512f, 8.05f)
                curveTo(8.512f, 4.475f, 11.425f, 1.563f, 15.0f, 1.563f)
                curveTo(18.574f, 1.563f, 21.487f, 4.475f, 21.487f, 8.05f)
                curveTo(21.475f, 11.563f, 18.725f, 14.413f, 15.237f, 14.525f)
                curveTo(15.224f, 14.525f, 15.212f, 14.525f, 15.2f, 14.525f)
                close()
                moveTo(15.0f, 3.438f)
                curveTo(12.462f, 3.438f, 10.387f, 5.512f, 10.387f, 8.05f)
                curveTo(10.387f, 10.55f, 12.337f, 12.563f, 14.825f, 12.65f)
                curveTo(14.887f, 12.637f, 15.062f, 12.637f, 15.224f, 12.65f)
                curveTo(17.674f, 12.538f, 19.6f, 10.525f, 19.612f, 8.05f)
                curveTo(19.612f, 5.512f, 17.537f, 3.438f, 15.0f, 3.438f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.212f, 28.188f)
                curveTo(12.762f, 28.188f, 10.3f, 27.563f, 8.437f, 26.313f)
                curveTo(6.7f, 25.163f, 5.75f, 23.587f, 5.75f, 21.875f)
                curveTo(5.75f, 20.163f, 6.7f, 18.575f, 8.437f, 17.413f)
                curveTo(12.187f, 14.925f, 18.262f, 14.925f, 21.987f, 17.413f)
                curveTo(23.712f, 18.563f, 24.674f, 20.138f, 24.674f, 21.85f)
                curveTo(24.674f, 23.563f, 23.725f, 25.15f, 21.987f, 26.313f)
                curveTo(20.112f, 27.563f, 17.662f, 28.188f, 15.212f, 28.188f)
                close()
                moveTo(9.475f, 18.987f)
                curveTo(8.275f, 19.788f, 7.625f, 20.813f, 7.625f, 21.888f)
                curveTo(7.625f, 22.95f, 8.287f, 23.975f, 9.475f, 24.763f)
                curveTo(12.587f, 26.85f, 17.837f, 26.85f, 20.949f, 24.763f)
                curveTo(22.149f, 23.962f, 22.799f, 22.938f, 22.799f, 21.862f)
                curveTo(22.799f, 20.8f, 22.137f, 19.775f, 20.949f, 18.987f)
                curveTo(17.837f, 16.913f, 12.587f, 16.913f, 9.475f, 18.987f)
                close()
            }
        }
        .build()
        return _navigationProfileIcon!!
    }

private var _navigationProfileIcon: ImageVector? = null
