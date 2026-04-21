package com.padabajka.dating.settings.presentation.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.layout.SimpleTopBar
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.settings.presentation.model.language.LanguageEvent
import com.padabajka.dating.settings.presentation.model.language.LanguageItem
import com.padabajka.dating.settings.presentation.model.language.LanguageState

@Composable
fun LanguageSelectorScreen(component: LanguageSelectorComponent) {
    val state by component.state.subscribeAsState()

    CustomScaffold(
        topBar = { TopBar(component::onEvent) }
    ) {
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 50.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Languages(
                languageState = state,
                onLanguageSelected = {
                    component.onEvent(LanguageEvent.ChangeLanguage(it))
                }
            )
        }
    }
}

@Composable
private fun Languages(
    languageState: LanguageState,
    modifier: Modifier = Modifier,
    onLanguageSelected: (Language.Static) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        languageState.languages.forEach {
            Language(
                language = it,
                selected = it.id == languageState.selectedLanguage,
                onClick = { onLanguageSelected(it.id) }
            )
        }
    }
}

@Composable
private fun Language(
    language: LanguageItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = language.name)
        if (selected) {
            Icon(
                iconData = Icons.Default.Done.toData(),
            )
        }
    }
}

@Composable
private fun TopBar(onEvent: (LanguageEvent) -> Unit) {
    SimpleTopBar(
        title = StaticTextId.UiId.Language.translate(),
        navigateBack = { onEvent(LanguageEvent.NavigateBack) }
    )
}
