package com.padabajka.dating.core.presentation.ui.drawable.icon.raw.rawicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.raw.RawIcons

public val RawIcons.ReactionLike: ImageVector
    get() {
        if (_reactionLike != null) {
            return _reactionLike!!
        }
        _reactionLike = Builder(name = "ReactionLike", defaultWidth = 20.0.dp, defaultHeight =
                21.0.dp, viewportWidth = 20.0f, viewportHeight = 21.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFE10000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(-1.094f, 0.302f)
                    horizontalLineToRelative(22.205f)
                    verticalLineToRelative(20.701f)
                    horizontalLineToRelative(-22.205f)
                    close()
                }
            }
        }
        .build()
        return _reactionLike!!
    }

private var _reactionLike: ImageVector? = null
