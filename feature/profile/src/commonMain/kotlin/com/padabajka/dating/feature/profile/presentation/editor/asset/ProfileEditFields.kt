package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun TextEditField(
    text: String,
    hint: String = "",
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    TextInputField(
        text = text,
        hint = hint,
        onChange = onChange,
        singleLine = false,
        shape = shape,
        modifier = modifier
            .innerShadow(
                color = Color(color = 0xFFA1A1A1),
                shape = shape
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}
