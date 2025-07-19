package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.TextInputField
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow

@Composable
fun SmallEditField(
    label: String,
    value: String,
    hint: String,
    onChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(label, modifier = Modifier.weight(1f))
        TextEditField(
            text = value,
            hint = hint,
            onChange = onChange,
            modifier = Modifier.weight(weight = 3f)
        )
    }
}

@Composable
fun SmallOnlyDataField(
    label: String,
    value: String,
    hint: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(label, modifier = Modifier.weight(1f))
        TextEditField(
            text = value,
            hint = hint,
            onChange = {},
            enabled = false,
            onClick = onClick,
            modifier = Modifier.weight(weight = 3f)
        )
    }
}

@Composable
fun TextEditField(
    text: String,
    hint: String = "",
    enabled: Boolean = true,
    onChange: (String) -> Unit,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val shape = RoundedCornerShape(20.dp)
    val optionalClickable: Modifier.() -> Modifier =
        { if (onClick != null) clickable(onClick = onClick) else this }
    TextInputField(
        text = text,
        hint = hint,
        onChange = onChange,
        singleLine = false,
        shape = shape,
        enabled = enabled,
        modifier = modifier
            .innerShadow(
                color = Color(color = 0xFFA1A1A1),
                shape = shape
            ).clip(shape).optionalClickable(),
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
