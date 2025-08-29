package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreTextEditField

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
        CoreTextEditField(
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
        CoreTextEditField(
            text = value,
            hint = hint,
            onChange = {},
            enabled = false,
            onClick = onClick,
            modifier = Modifier.weight(weight = 3f)
        )
    }
}
