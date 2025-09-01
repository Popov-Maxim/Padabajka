package com.padabajka.dating.feature.profile.presentation.creator.birthday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CoreTextEditField
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.repository.api.model.profile.toMillis
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileButton
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen
import com.padabajka.dating.feature.profile.presentation.creator.birthday.model.BirthdayItem
import com.padabajka.dating.feature.profile.presentation.creator.birthday.model.CreateProfileBirthdayEvent
import com.padabajka.dating.feature.profile.presentation.creator.birthday.model.CreateProfileBirthdayState
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset

@Composable
fun CreateProfileBirthdayScreen(component: CreateProfileBirthdayScreenComponent) {
    CreateProfileScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(Modifier.size(60.dp)) // TODO: add head

            MainContent(
                component = component,
                modifier = Modifier.weight(1f),
            )

            val state by component.state.subscribeAsState()

            CreateProfileButton(
                text = StaticTextId.UiId.ContinueBtn.translate(),
                modifier = Modifier,
                enabled = state.birthdayField.value != null &&
                    state.birthdayField.currentIssue == null,
                onClick = {
                    component.onEvent(CreateProfileBirthdayEvent.PressContinue)
                }
            )
        }
    }
}

@Composable
private fun MainContent(
    component: CreateProfileBirthdayScreenComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()

    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 45.dp).align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            TextAsset(
                title = StaticTextId.UiId.WhenBirthdayTitle.translate(),
                body = StaticTextId.UiId.WhenBirthdayBody.translate(),
                modifier = Modifier.fillMaxWidth()
            )

            BirthDayField(state, component::onEvent)
        }

        AgeHilt(state, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun AgeHilt(
    state: CreateProfileBirthdayState,
    modifier: Modifier = Modifier
) {
    val currentIssue = state.birthdayField.currentIssue
    val ageHilt = currentIssue?.toMessage() ?: state.birthdayField.value?.age?.raw?.toString()
    val hiltColor = if (currentIssue != null) CoreColors.primary.mainColor else CoreColors.background.textColor
    if (ageHilt != null) {
        Text(
            text = ageHilt,
            color = hiltColor,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth().padding(horizontal = 45.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDayField(
    state: CreateProfileBirthdayState,
    onEvent: (CreateProfileBirthdayEvent) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    CoreTextEditField(
        text = state.birthdayField.value?.date?.toString() ?: "",
        enabled = false,
        hint = StaticTextId.UiId.HintForBirthday.translate(),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            showDatePicker = true
        },
        onChange = {}
    )

    if (showDatePicker) {
        DatePickerModal(
            birthday = state.birthdayField.value,
            onDateSelected = { date ->
                onEvent(CreateProfileBirthdayEvent.BirthdayFieldUpdate(date))
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    birthday: BirthdayItem?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = birthday?.date?.toMillis(),
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val selectedDateMillis = datePickerState.selectedDateMillis
            if (selectedDateMillis != null) {
                TextButton(onClick = {
                    onDateSelected(selectedDateMillis)
                    onDismiss()
                }) {
                    Text("OK") // TODO: improve date picker
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
