package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape

@Composable
fun TextInputField(
    text: String,
    hint: String,
    onChange: (String) -> Unit,
    enabled: Boolean = true,
    onFocusLost: () -> Unit = {},
    onFocus: () -> Unit = {},
    isError: Boolean = false,
    singleLine: Boolean = true,
    shape: Shape = TextFieldDefaults.shape,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val wasFocused = remember { mutableStateOf(false) }
    TextField(
        value = text,
        onValueChange = onChange,
        singleLine = singleLine,
        shape = shape,
        isError = isError,
        enabled = enabled,
        modifier = modifier.onFocusChanged {
            if (!it.isFocused && wasFocused.value) {
                onFocusLost()
            } else if (it.isFocused && !wasFocused.value) {
                onFocus()
            }
            wasFocused.value = it.isFocused
        },
        placeholder = { Text(hint) },
        colors = colors,
        keyboardOptions = keyboardOptions
    )
}
