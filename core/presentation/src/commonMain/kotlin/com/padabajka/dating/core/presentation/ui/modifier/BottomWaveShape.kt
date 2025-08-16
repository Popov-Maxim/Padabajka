package com.padabajka.dating.core.presentation.ui.modifier

import androidx.compose.foundation.shape.GenericShape

@Suppress("MagicNumber")
val BottomWaveShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height - 40)

    quadraticTo(
        x1 = size.width / 2,
        y1 = size.height - 60,
        x2 = 0f,
        y2 = size.height + 40
    )
    close()
}
