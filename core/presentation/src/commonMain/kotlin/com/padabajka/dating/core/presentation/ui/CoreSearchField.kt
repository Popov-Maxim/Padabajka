package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CoreSearchField(
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
    text: String,
    onChange: (String) -> Unit,
    hint: String
) {
    CoreTextEditField(
        modifier = modifier,
        text = text,
        onChange = onChange,
        hint = hint
    )
}
