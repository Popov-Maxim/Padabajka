package com.padabajka.dating.core.presentation.ui.modifier

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.optionalClickable(
    onClick: (() -> Unit)?,
    enabled: Boolean = true
): Modifier = composed {
    if (onClick != null && enabled) {
        this.clickable(onClick = onClick)
    } else {
        this
    }
}
