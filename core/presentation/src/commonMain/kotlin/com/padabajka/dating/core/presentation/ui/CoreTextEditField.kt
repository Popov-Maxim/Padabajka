package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.presentation.ui.modifier.optionalClickable

@Composable
fun CoreTextEditField(
    text: String,
    hint: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    onChange: (String) -> Unit,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val shape = RoundedCornerShape(20.dp)
    TextInputField(
        text = text,
        hint = hint,
        onChange = onChange,
        singleLine = singleLine,
        shape = shape,
        enabled = enabled,
        modifier = modifier
            .innerShadow(
                color = Color(color = 0xFFA1A1A1),
                shape = shape
            ).clip(shape).optionalClickable(onClick)
            .then(modifierAfter),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = keyboardOptions
    )
}
