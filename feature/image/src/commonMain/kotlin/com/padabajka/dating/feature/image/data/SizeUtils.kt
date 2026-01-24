package com.padabajka.dating.feature.image.data

import kotlin.math.sqrt

object SizeUtils {
    private const val MEGAPIXEL = 1_000_000f

    fun calculateNewDimensions(
        width: Int,
        height: Int,
        maxMegapixels: Float
    ): Pair<Int, Int> {
        val currentMP = width.toFloat() * height / MEGAPIXEL
        if (currentMP <= maxMegapixels) return width to height

        val scale = sqrt(maxMegapixels / currentMP)
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        return newWidth to newHeight
    }
}
