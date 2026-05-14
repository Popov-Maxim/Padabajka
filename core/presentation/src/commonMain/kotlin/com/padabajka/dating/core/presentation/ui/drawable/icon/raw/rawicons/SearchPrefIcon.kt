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

public val RawIcons.SearchPrefIcon: ImageVector
    get() {
        if (_searchPrefIcon != null) {
            return _searchPrefIcon!!
        }
        _searchPrefIcon = Builder(name = "SearchPrefIcon", defaultWidth = 30.0.dp, defaultHeight =
                30.0.dp, viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(27.5f, 22.813f)
                horizontalLineTo(18.75f)
                curveTo(18.237f, 22.813f, 17.813f, 22.388f, 17.813f, 21.875f)
                curveTo(17.813f, 21.362f, 18.237f, 20.938f, 18.75f, 20.938f)
                horizontalLineTo(27.5f)
                curveTo(28.013f, 20.938f, 28.438f, 21.362f, 28.438f, 21.875f)
                curveTo(28.438f, 22.388f, 28.013f, 22.813f, 27.5f, 22.813f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(6.25f, 22.813f)
                horizontalLineTo(2.5f)
                curveTo(1.987f, 22.813f, 1.563f, 22.388f, 1.563f, 21.875f)
                curveTo(1.563f, 21.362f, 1.987f, 20.938f, 2.5f, 20.938f)
                horizontalLineTo(6.25f)
                curveTo(6.762f, 20.938f, 7.188f, 21.362f, 7.188f, 21.875f)
                curveTo(7.188f, 22.388f, 6.762f, 22.813f, 6.25f, 22.813f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(27.5f, 9.063f)
                horizontalLineTo(23.75f)
                curveTo(23.237f, 9.063f, 22.813f, 8.637f, 22.813f, 8.125f)
                curveTo(22.813f, 7.613f, 23.237f, 7.188f, 23.75f, 7.188f)
                horizontalLineTo(27.5f)
                curveTo(28.013f, 7.188f, 28.438f, 7.613f, 28.438f, 8.125f)
                curveTo(28.438f, 8.637f, 28.013f, 9.063f, 27.5f, 9.063f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(11.25f, 9.063f)
                horizontalLineTo(2.5f)
                curveTo(1.987f, 9.063f, 1.563f, 8.637f, 1.563f, 8.125f)
                curveTo(1.563f, 7.613f, 1.987f, 7.188f, 2.5f, 7.188f)
                horizontalLineTo(11.25f)
                curveTo(11.762f, 7.188f, 12.188f, 7.613f, 12.188f, 8.125f)
                curveTo(12.188f, 8.637f, 11.762f, 9.063f, 11.25f, 9.063f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(16.25f, 26.563f)
                horizontalLineTo(8.75f)
                curveTo(6.6f, 26.563f, 5.313f, 25.275f, 5.313f, 23.125f)
                verticalLineTo(20.625f)
                curveTo(5.313f, 18.475f, 6.6f, 17.188f, 8.75f, 17.188f)
                horizontalLineTo(16.25f)
                curveTo(18.4f, 17.188f, 19.688f, 18.475f, 19.688f, 20.625f)
                verticalLineTo(23.125f)
                curveTo(19.688f, 25.275f, 18.4f, 26.563f, 16.25f, 26.563f)
                close()
                moveTo(8.75f, 19.063f)
                curveTo(7.637f, 19.063f, 7.188f, 19.513f, 7.188f, 20.625f)
                verticalLineTo(23.125f)
                curveTo(7.188f, 24.237f, 7.637f, 24.688f, 8.75f, 24.688f)
                horizontalLineTo(16.25f)
                curveTo(17.362f, 24.688f, 17.813f, 24.237f, 17.813f, 23.125f)
                verticalLineTo(20.625f)
                curveTo(17.813f, 19.513f, 17.362f, 19.063f, 16.25f, 19.063f)
                horizontalLineTo(8.75f)
                close()
            }
            path(fill = SolidColor(Color(0xFF21272A)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(21.25f, 12.813f)
                horizontalLineTo(13.75f)
                curveTo(11.6f, 12.813f, 10.313f, 11.525f, 10.313f, 9.375f)
                verticalLineTo(6.875f)
                curveTo(10.313f, 4.725f, 11.6f, 3.438f, 13.75f, 3.438f)
                horizontalLineTo(21.25f)
                curveTo(23.4f, 3.438f, 24.688f, 4.725f, 24.688f, 6.875f)
                verticalLineTo(9.375f)
                curveTo(24.688f, 11.525f, 23.4f, 12.813f, 21.25f, 12.813f)
                close()
                moveTo(13.75f, 5.313f)
                curveTo(12.637f, 5.313f, 12.188f, 5.762f, 12.188f, 6.875f)
                verticalLineTo(9.375f)
                curveTo(12.188f, 10.488f, 12.637f, 10.938f, 13.75f, 10.938f)
                horizontalLineTo(21.25f)
                curveTo(22.362f, 10.938f, 22.813f, 10.488f, 22.813f, 9.375f)
                verticalLineTo(6.875f)
                curveTo(22.813f, 5.762f, 22.362f, 5.313f, 21.25f, 5.313f)
                horizontalLineTo(13.75f)
                close()
            }
        }
        .build()
        return _searchPrefIcon!!
    }

private var _searchPrefIcon: ImageVector? = null
