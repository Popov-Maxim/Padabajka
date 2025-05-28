package com.padabajka.dating.core.presentation.ui.drawable.icon

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface IconData {
    data class Painter(val painter: androidx.compose.ui.graphics.painter.Painter) : IconData
    data class Vector(val vector: ImageVector) : IconData
    data object Empty : IconData
}

fun Painter.toData() = IconData.Painter(this)
fun ImageVector.toData() = IconData.Vector(this)
