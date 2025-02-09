package com.fp.padabajka.feature.profile.presentation.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fp.padabajka.core.presentation.ui.TextInputField

@Composable
fun ProfileEditorScreen() {
    Column {
        var firstName by remember { mutableStateOf("") }
        val modifier = Modifier.height(70.dp).padding(10.dp)
        TextEditField(
            text = firstName,
            label = "First Name",
            onChange = {
                firstName = it
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))

        var lastName by remember { mutableStateOf("") }
        TextEditField(
            text = lastName,
            label = "Last Name",
            onChange = {
                lastName = it
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))

        var aboutMe by remember { mutableStateOf("") }
        TextEditField(
            text = aboutMe,
            label = "About Me",
            onChange = {
                aboutMe = it
            },
            modifier = modifier
        )
    }
}

@Composable
private fun TextEditField(
    text: String,
    label: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Text(
                text = label,
                modifier = Modifier.wrapContentSize().align(Alignment.CenterStart)
            )
        }
        TextInputField(
            text = text,
            hint = "",
            onChange = onChange,
            modifier = Modifier.weight(2f).fillMaxHeight()
        )
    }
}
