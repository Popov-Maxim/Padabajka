package com.fp.padabajka.feature.profile.presentation.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.core.presentation.ui.TextInputField
import com.fp.padabajka.feature.profile.presentation.editor.model.AboutMeFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.DiscardProfileUpdatesClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.FirstNameFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.LastNameFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.SaveProfileUpdatesClickEvent

@Composable
fun ProfileEditorScreen(component: ProfileEditorScreenComponent) {
    val state by component.state.subscribeAsState()
    Column {
        val firstName = state.firstName.value
        val modifier = Modifier.height(70.dp).padding(10.dp)
        TextEditField(
            text = firstName,
            label = "First Name",
            onChange = {
                component.onEvent(FirstNameFieldUpdateEvent(it))
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))

        val lastName = state.lastName.value
        TextEditField(
            text = lastName,
            label = "Last Name",
            onChange = {
                component.onEvent(LastNameFieldUpdateEvent(it))
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))

        val aboutMe = state.aboutMe.value
        TextEditField(
            text = aboutMe,
            label = "About Me",
            onChange = {
                component.onEvent(AboutMeFieldUpdateEvent(it))
            },
            modifier = modifier
        )

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    component.onEvent(DiscardProfileUpdatesClickEvent)
                }
            ) {
                Text(
                    text = "Сбросить"
                )
            }

            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    component.onEvent(SaveProfileUpdatesClickEvent)
                }
            ) {
                Text(
                    text = "Принять"
                )
            }
        }
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
