package com.padabajka.dating.feature.profile.presentation.creator.name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreTextEditField
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileButton
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen
import com.padabajka.dating.feature.profile.presentation.creator.name.model.CreateProfileNameEvent

@Composable
fun CreateProfileNameScreen(component: CreateProfileNameScreenComponent) {
    CreateProfileScreen {
        Box(Modifier.fillMaxSize()) {
            MainContent(
                component = component,
                modifier = Modifier.align(Alignment.Center),
            )

            CreateProfileButton(
                text = StaticTextId.UiId.ContinueBtn.translate(),
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    component.onEvent(CreateProfileNameEvent.PressContinue)
                }
            )
        }
    }
}

@Composable
private fun MainContent(
    component: CreateProfileNameScreenComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()

    Column(
        modifier = modifier.padding(horizontal = 45.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        TextAsset(
            title = StaticTextId.UiId.HowNameTitle.translate(),
            body = StaticTextId.UiId.HowNameBody.translate(),
            modifier = Modifier.fillMaxWidth()
        )

        CoreTextEditField(
            text = state.nameField.value,
            hint = StaticTextId.UiId.HintForName.translate(),
            modifier = Modifier.fillMaxWidth(),
            onChange = { component.onEvent(CreateProfileNameEvent.NameFieldUpdate(it)) }
        )
    }
}

@Composable
fun TextAsset(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            lineHeight = 30.sp * 1.2,
            textAlign = TextAlign.Center,
        ) // TODO(font): standardize font in all project, may be monospace?

        Text(
            text = body,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Black.copy(alpha = 0.6f)
        )
    }
}
