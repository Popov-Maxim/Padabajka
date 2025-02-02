package com.fp.padabajka.core.presentation.ui

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Suppress("UnusedParameter")
@Composable
fun TextInputField(
    text: String,
    hint: String,
    onChange: (String) -> Unit,
    onFocusLost: () -> Unit = {},
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val wasFocused = remember { mutableStateOf(false) }
    TextField(
        value = text,
        onValueChange = onChange,
        singleLine = true,
        isError = isError,
        modifier = modifier.onFocusChanged {
            if (!it.isFocused && wasFocused.value) {
                onFocusLost()
            }
            wasFocused.value = it.isFocused
        }
    )
}
