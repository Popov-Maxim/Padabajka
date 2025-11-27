package com.padabajka.dating.core.presentation.ui.modifier

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

fun Modifier.hideKeyboardOnTap(): Modifier = composed {
    val focusManager = LocalFocusManager.current

    this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }
}
