package com.padabajka.dating.feature.profile.presentation.creator.lookingfor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen
import com.padabajka.dating.feature.profile.presentation.creator.lookingfor.model.CreateProfileLookingForEvent
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset
import com.padabajka.dating.feature.profile.presentation.editor.dialog.DetailsScreen
import com.padabajka.dating.feature.profile.presentation.editor.dialog.TypeScreen
import com.padabajka.dating.feature.profile.presentation.editor.dialog.lookingForAssets

@Composable
fun CreateProfileLookingForTypeSelectorScreen(
    onTypeSelected: (StaticTextId) -> Unit
) {
    CreateProfileScreen {
        TypeSelectorScreen(
            onTypeClick = onTypeSelected
        )
    }
}

@Composable
fun CreateProfileLookingForDetailSelectorScreen(
    component: CreateProfileLookingForScreenComponent,
) {
    val state by component.state.subscribeAsState()

    CreateProfileScreen {
        DetailSelectorScreen(
            selectedType = state.selectedType,
            onDetailsClick = {
                component.onEvent(CreateProfileLookingForEvent.OnDetailSelected(it))
            }
        )
    }
}

@Composable
private fun TypeSelectorScreen(
    modifier: Modifier = Modifier,
    onTypeClick: (StaticTextId) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(Modifier.size(60.dp)) // TODO: add head

        Column(
            modifier = Modifier.weight(1f).fillMaxSize().padding(vertical = 50.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 50.dp,
            )
        ) {
            TextAsset(
                title = StaticTextId.UiId.WhatLookingForTitle.translate(),
                body = StaticTextId.UiId.WhatLookingForBody.translate(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 36.dp)
            )

            TypeScreen(
                modifier = Modifier.padding(horizontal = 20.dp),
                onTypeClick = onTypeClick
            )
        }
    }
}

@Composable
private fun DetailSelectorScreen(
    selectedType: StaticTextId,
    modifier: Modifier = Modifier,
    onDetailsClick: (StaticTextId) -> Unit
) {
    val details = lookingForAssets[selectedType]!!
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(Modifier.size(60.dp)) // TODO: add head

        Column(
            modifier = Modifier.fillMaxSize().weight(1f).padding(vertical = 50.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 50.dp,
            )
        ) {
            TextAsset(
                title = selectedType.translate(),
                body = StaticTextId.UiId.WhatLookingForBody.translate(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 36.dp)
            )

            DetailsScreen(
                details = details,
                modifier = Modifier.padding(horizontal = 20.dp),
                onDetailsClick = onDetailsClick
            )
        }
    }
}
