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

public val RawIcons.NavigationLikesIcon: ImageVector
    get() {
        if (_navigationLikesIcon != null) {
            return _navigationLikesIcon!!
        }
        _navigationLikesIcon = Builder(name = "NavigationLikesIcon", defaultWidth = 30.0.dp,
                defaultHeight = 30.0.dp, viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(20.35f, 27.625f)
                horizontalLineTo(15.6f)
                curveTo(14.9f, 27.625f, 13.375f, 27.413f, 12.563f, 26.6f)
                lineTo(8.775f, 23.675f)
                lineTo(9.925f, 22.188f)
                lineTo(13.8f, 25.188f)
                curveTo(14.113f, 25.487f, 14.9f, 25.737f, 15.6f, 25.737f)
                horizontalLineTo(20.35f)
                curveTo(21.475f, 25.737f, 22.688f, 24.837f, 22.938f, 23.825f)
                lineTo(25.962f, 14.637f)
                curveTo(26.163f, 14.087f, 26.125f, 13.587f, 25.862f, 13.225f)
                curveTo(25.587f, 12.837f, 25.087f, 12.613f, 24.475f, 12.613f)
                horizontalLineTo(19.475f)
                curveTo(18.825f, 12.613f, 18.225f, 12.337f, 17.813f, 11.863f)
                curveTo(17.388f, 11.375f, 17.2f, 10.725f, 17.3f, 10.05f)
                lineTo(17.925f, 6.037f)
                curveTo(18.075f, 5.337f, 17.6f, 4.55f, 16.925f, 4.325f)
                curveTo(16.313f, 4.1f, 15.525f, 4.425f, 15.25f, 4.825f)
                lineTo(10.125f, 12.45f)
                lineTo(8.575f, 11.413f)
                lineTo(13.7f, 3.787f)
                curveTo(14.488f, 2.612f, 16.212f, 2.05f, 17.563f, 2.562f)
                curveTo(19.125f, 3.075f, 20.125f, 4.8f, 19.775f, 6.4f)
                lineTo(19.163f, 10.337f)
                curveTo(19.15f, 10.425f, 19.15f, 10.55f, 19.237f, 10.65f)
                curveTo(19.3f, 10.712f, 19.388f, 10.75f, 19.487f, 10.75f)
                horizontalLineTo(24.487f)
                curveTo(25.712f, 10.75f, 26.775f, 11.262f, 27.4f, 12.15f)
                curveTo(28.013f, 13.012f, 28.138f, 14.15f, 27.737f, 15.25f)
                lineTo(24.75f, 24.35f)
                curveTo(24.288f, 26.163f, 22.362f, 27.625f, 20.35f, 27.625f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(6.725f, 26.25f)
                horizontalLineTo(5.475f)
                curveTo(3.163f, 26.25f, 2.038f, 25.163f, 2.038f, 22.938f)
                verticalLineTo(10.688f)
                curveTo(2.038f, 8.462f, 3.163f, 7.375f, 5.475f, 7.375f)
                horizontalLineTo(6.725f)
                curveTo(9.038f, 7.375f, 10.163f, 8.462f, 10.163f, 10.688f)
                verticalLineTo(22.938f)
                curveTo(10.163f, 25.163f, 9.038f, 26.25f, 6.725f, 26.25f)
                close()
                moveTo(5.475f, 9.25f)
                curveTo(4.113f, 9.25f, 3.913f, 9.575f, 3.913f, 10.688f)
                verticalLineTo(22.938f)
                curveTo(3.913f, 24.05f, 4.113f, 24.375f, 5.475f, 24.375f)
                horizontalLineTo(6.725f)
                curveTo(8.088f, 24.375f, 8.288f, 24.05f, 8.288f, 22.938f)
                verticalLineTo(10.688f)
                curveTo(8.288f, 9.575f, 8.088f, 9.25f, 6.725f, 9.25f)
                horizontalLineTo(5.475f)
                close()
            }
        }
        .build()
        return _navigationLikesIcon!!
    }

private var _navigationLikesIcon: ImageVector? = null
