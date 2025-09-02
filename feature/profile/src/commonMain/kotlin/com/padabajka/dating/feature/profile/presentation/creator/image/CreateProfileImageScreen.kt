package com.padabajka.dating.feature.profile.presentation.creator.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileButton
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen
import com.padabajka.dating.feature.profile.presentation.creator.image.model.CreateProfileImageEvent
import com.padabajka.dating.feature.profile.presentation.creator.image.model.CreateProfileImageState
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset
import com.padabajka.dating.feature.profile.presentation.editor.ImageField

@Composable
fun CreateProfileImageScreen(component: CreateProfileImageScreenComponent) {
    CreateProfileScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(Modifier.size(45.dp)) // TODO: add head

            val state by component.state.subscribeAsState()

            MainContent(
                modifier = Modifier.weight(1f),
                state = state,
                onEvent = component::onEvent
            )

            CreateProfileButton(
                text = StaticTextId.UiId.ContinueBtn.translate(),
                modifier = Modifier,
                enabled = state.image != null,
                onClick = {
                    component.onEvent(CreateProfileImageEvent.Continue)
                }
            )
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    state: CreateProfileImageState,
    onEvent: (CreateProfileImageEvent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(vertical = 30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextAsset(
            title = StaticTextId.UiId.WhatAvatarTitle.translate(),
            body = StaticTextId.UiId.WhatAvatarBody.translate(),
            modifier = Modifier.fillMaxWidth().padding(36.dp)
        )

        ImageField(
            image = state.image,
            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            iconSize = 100.dp,
            onChange = {
                onEvent(CreateProfileImageEvent.ImageSelected(it))
            },
            delete = {
                onEvent(CreateProfileImageEvent.DeleteImage)
            }
        )
    }
}
