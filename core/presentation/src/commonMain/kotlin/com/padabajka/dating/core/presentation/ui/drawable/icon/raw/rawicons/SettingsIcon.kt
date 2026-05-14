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

public val RawIcons.SettingsIcon: ImageVector
    get() {
        if (_settingsIcon != null) {
            return _settingsIcon!!
        }
        _settingsIcon = Builder(name = "SettingsIcon", defaultWidth = 30.0.dp, defaultHeight =
                30.0.dp, viewportWidth = 30.0f, viewportHeight = 30.0f).apply {
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(15.0f, 19.688f)
                curveTo(12.413f, 19.688f, 10.313f, 17.587f, 10.313f, 15.0f)
                curveTo(10.313f, 12.413f, 12.413f, 10.313f, 15.0f, 10.313f)
                curveTo(17.587f, 10.313f, 19.688f, 12.413f, 19.688f, 15.0f)
                curveTo(19.688f, 17.587f, 17.587f, 19.688f, 15.0f, 19.688f)
                close()
                moveTo(15.0f, 12.188f)
                curveTo(13.45f, 12.188f, 12.188f, 13.45f, 12.188f, 15.0f)
                curveTo(12.188f, 16.55f, 13.45f, 17.813f, 15.0f, 17.813f)
                curveTo(16.55f, 17.813f, 17.813f, 16.55f, 17.813f, 15.0f)
                curveTo(17.813f, 13.45f, 16.55f, 12.188f, 15.0f, 12.188f)
                close()
            }
            path(fill = SolidColor(Color(0xFF292D32)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(19.013f, 27.737f)
                curveTo(18.75f, 27.737f, 18.487f, 27.7f, 18.225f, 27.638f)
                curveTo(17.45f, 27.425f, 16.8f, 26.938f, 16.388f, 26.25f)
                lineTo(16.237f, 26.0f)
                curveTo(15.5f, 24.725f, 14.488f, 24.725f, 13.75f, 26.0f)
                lineTo(13.613f, 26.237f)
                curveTo(13.2f, 26.938f, 12.55f, 27.438f, 11.775f, 27.638f)
                curveTo(10.988f, 27.85f, 10.175f, 27.737f, 9.488f, 27.325f)
                lineTo(7.338f, 26.087f)
                curveTo(6.575f, 25.65f, 6.025f, 24.938f, 5.787f, 24.075f)
                curveTo(5.563f, 23.212f, 5.675f, 22.325f, 6.113f, 21.563f)
                curveTo(6.475f, 20.925f, 6.575f, 20.35f, 6.363f, 19.987f)
                curveTo(6.15f, 19.625f, 5.613f, 19.413f, 4.875f, 19.413f)
                curveTo(3.05f, 19.413f, 1.563f, 17.925f, 1.563f, 16.1f)
                verticalLineTo(13.9f)
                curveTo(1.563f, 12.075f, 3.05f, 10.587f, 4.875f, 10.587f)
                curveTo(5.613f, 10.587f, 6.15f, 10.375f, 6.363f, 10.012f)
                curveTo(6.575f, 9.65f, 6.488f, 9.075f, 6.113f, 8.438f)
                curveTo(5.675f, 7.675f, 5.563f, 6.775f, 5.787f, 5.925f)
                curveTo(6.012f, 5.063f, 6.563f, 4.35f, 7.338f, 3.912f)
                lineTo(9.5f, 2.675f)
                curveTo(10.913f, 1.837f, 12.775f, 2.325f, 13.625f, 3.763f)
                lineTo(13.775f, 4.012f)
                curveTo(14.512f, 5.287f, 15.525f, 5.287f, 16.263f, 4.012f)
                lineTo(16.4f, 3.775f)
                curveTo(17.25f, 2.325f, 19.112f, 1.837f, 20.538f, 2.688f)
                lineTo(22.688f, 3.925f)
                curveTo(23.45f, 4.363f, 24.0f, 5.075f, 24.237f, 5.938f)
                curveTo(24.462f, 6.8f, 24.35f, 7.688f, 23.913f, 8.45f)
                curveTo(23.55f, 9.087f, 23.45f, 9.663f, 23.663f, 10.025f)
                curveTo(23.875f, 10.387f, 24.413f, 10.6f, 25.15f, 10.6f)
                curveTo(26.975f, 10.6f, 28.462f, 12.087f, 28.462f, 13.913f)
                verticalLineTo(16.112f)
                curveTo(28.462f, 17.938f, 26.975f, 19.425f, 25.15f, 19.425f)
                curveTo(24.413f, 19.425f, 23.875f, 19.638f, 23.663f, 20.0f)
                curveTo(23.45f, 20.362f, 23.538f, 20.938f, 23.913f, 21.575f)
                curveTo(24.35f, 22.337f, 24.475f, 23.237f, 24.237f, 24.087f)
                curveTo(24.013f, 24.95f, 23.462f, 25.663f, 22.688f, 26.1f)
                lineTo(20.525f, 27.337f)
                curveTo(20.05f, 27.6f, 19.538f, 27.737f, 19.013f, 27.737f)
                close()
                moveTo(15.0f, 23.112f)
                curveTo(16.112f, 23.112f, 17.15f, 23.813f, 17.862f, 25.05f)
                lineTo(18.0f, 25.288f)
                curveTo(18.15f, 25.55f, 18.4f, 25.737f, 18.7f, 25.813f)
                curveTo(19.0f, 25.888f, 19.3f, 25.85f, 19.55f, 25.7f)
                lineTo(21.712f, 24.45f)
                curveTo(22.038f, 24.263f, 22.288f, 23.95f, 22.388f, 23.575f)
                curveTo(22.487f, 23.2f, 22.438f, 22.813f, 22.25f, 22.487f)
                curveTo(21.538f, 21.263f, 21.45f, 20.0f, 22.0f, 19.038f)
                curveTo(22.55f, 18.075f, 23.688f, 17.525f, 25.112f, 17.525f)
                curveTo(25.913f, 17.525f, 26.55f, 16.888f, 26.55f, 16.087f)
                verticalLineTo(13.887f)
                curveTo(26.55f, 13.1f, 25.913f, 12.45f, 25.112f, 12.45f)
                curveTo(23.688f, 12.45f, 22.55f, 11.9f, 22.0f, 10.938f)
                curveTo(21.45f, 9.975f, 21.538f, 8.712f, 22.25f, 7.488f)
                curveTo(22.438f, 7.162f, 22.487f, 6.775f, 22.388f, 6.4f)
                curveTo(22.288f, 6.025f, 22.05f, 5.725f, 21.725f, 5.525f)
                lineTo(19.563f, 4.287f)
                curveTo(19.025f, 3.963f, 18.313f, 4.15f, 17.987f, 4.7f)
                lineTo(17.85f, 4.938f)
                curveTo(17.138f, 6.175f, 16.1f, 6.875f, 14.988f, 6.875f)
                curveTo(13.875f, 6.875f, 12.837f, 6.175f, 12.125f, 4.938f)
                lineTo(11.988f, 4.688f)
                curveTo(11.675f, 4.162f, 10.975f, 3.975f, 10.438f, 4.287f)
                lineTo(8.275f, 5.537f)
                curveTo(7.95f, 5.725f, 7.7f, 6.037f, 7.6f, 6.412f)
                curveTo(7.5f, 6.787f, 7.55f, 7.175f, 7.738f, 7.5f)
                curveTo(8.45f, 8.725f, 8.538f, 9.988f, 7.988f, 10.95f)
                curveTo(7.438f, 11.913f, 6.3f, 12.462f, 4.875f, 12.462f)
                curveTo(4.075f, 12.462f, 3.438f, 13.1f, 3.438f, 13.9f)
                verticalLineTo(16.1f)
                curveTo(3.438f, 16.888f, 4.075f, 17.538f, 4.875f, 17.538f)
                curveTo(6.3f, 17.538f, 7.438f, 18.087f, 7.988f, 19.05f)
                curveTo(8.538f, 20.013f, 8.45f, 21.275f, 7.738f, 22.5f)
                curveTo(7.55f, 22.825f, 7.5f, 23.212f, 7.6f, 23.587f)
                curveTo(7.7f, 23.962f, 7.938f, 24.263f, 8.262f, 24.462f)
                lineTo(10.425f, 25.7f)
                curveTo(10.688f, 25.862f, 11.0f, 25.9f, 11.288f, 25.825f)
                curveTo(11.587f, 25.75f, 11.837f, 25.55f, 12.0f, 25.288f)
                lineTo(12.137f, 25.05f)
                curveTo(12.85f, 23.825f, 13.887f, 23.112f, 15.0f, 23.112f)
                close()
            }
        }
        .build()
        return _settingsIcon!!
    }

private var _settingsIcon: ImageVector? = null
