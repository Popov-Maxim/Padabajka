package com.padabajka.dating.core.presentation.ui.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
fun Modifier.applyIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.block()
    } else {
        this
    }
}
