package com.padabajka.dating.feature.profile.presentation.creator.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileButton
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.GenderSelectorEvent
import com.padabajka.dating.feature.profile.presentation.creator.gender.ui.GenderSelector
import com.padabajka.dating.feature.profile.presentation.creator.gender.ui.PreferredGenderSelector
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset

@Composable
fun CreateProfileSexScreen(component: CreateProfileSexScreenComponent) {
    CreateProfileScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(Modifier.size(60.dp)) // TODO: add head

            MainContent(
                modifier = Modifier.weight(1f),
                component = component
            )

            val state by component.state.subscribeAsState()

            CreateProfileButton(
                text = StaticTextId.UiId.ContinueBtn.translate(),
                modifier = Modifier,
                enabled = state.userGender != null && state.preferredGender != null,
                onClick = {
                    component.onEvent(GenderSelectorEvent.PressContinue)
                }
            )
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    component: CreateProfileSexScreenComponent
) {
    Column(modifier.fillMaxSize()) {
        BiologicalSexSelector(
            modifier = Modifier.weight(1f).padding(horizontal = 45.dp),
            component = component
        )
        PreferenceSelector(
            modifier = Modifier.weight(1f).padding(horizontal = 45.dp),
            component = component
        )
    }
}

@Composable
private fun BiologicalSexSelector(
    modifier: Modifier = Modifier,
    component: CreateProfileSexScreenComponent
) {
    val state by component.state.subscribeAsState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextAsset(
            title = StaticTextId.UiId.WhatBiologicalSexTitle.translate(),
            body = StaticTextId.UiId.WhatBiologicalSexBody.translate(),
            modifier = Modifier.fillMaxWidth()
        )

        GenderSelector(
            selectedGender = state.userGender,
            select = { component.onEvent(GenderSelectorEvent.UserGenderSelected(it)) }
        )
    }
}

@Composable
private fun PreferenceSelector(
    modifier: Modifier = Modifier,
    component: CreateProfileSexScreenComponent
) {
    val state by component.state.subscribeAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextAsset(
            title = StaticTextId.UiId.WhatPreferenceTitle.translate(),
            body = StaticTextId.UiId.WhatPreferenceBody.translate(),
            modifier = Modifier.fillMaxWidth()
        )
        PreferredGenderSelector(
            selectedGender = state.preferredGender,
            select = { component.onEvent(GenderSelectorEvent.PreferredGenderSelected(it)) }
        )
    }
}
