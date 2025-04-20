package com.padabajka.dating.core.presentation.ui

import androidx.compose.ui.graphics.Color

@Suppress("MagicNumber")
object CoreColors {
    private const val totalVariant: Long = 0x32985353

    val background = ColorGroup(
        0xFFFBFBFF,
        0xFF000000,
        totalVariant,
    )
    val primary = ColorGroup(
        0xFFFF4D50,
        0xFFFFFFFF,
        totalVariant,
    )
    val secondary = ColorGroup(
        0xFF6EA3CA,
        0xFFFFFFFF,
        totalVariant,
    )

    object Chat {
        val secondary = ColorGroup(
            0xFF7561FF,
            0xFFFFFFFF,
            totalVariant,
        )

        val message = secondary
    }
}

data class ColorGroup(
    val main: Long,
    val text: Long,
    val variant: Long
)

val ColorGroup.mainColor: Color
    get() = Color(this.main)
val ColorGroup.textColor: Color
    get() = Color(this.text)
val ColorGroup.variantColor: Color
    get() = Color(this.variant)
