package com.padabajka.dating.feature.profile.presentation.creator.finish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreCircularProgressIndicator
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen
import com.padabajka.dating.feature.profile.presentation.creator.finish.model.CreateProfileFinishEvent
import com.padabajka.dating.feature.profile.presentation.creator.finish.model.CreateProfileFinishState

@Composable
fun CreateProfileFinishScreen(component: CreateProfileFinishScreenComponent) {
    val state by component.state.subscribeAsState()

    CreateProfileScreen {
        when (val value = state) {
            CreateProfileFinishState.Loading -> LoadingContent()
            is CreateProfileFinishState.Error -> ErrorContent(
                state = value,
                onEvent = component::onEvent
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 45.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 50.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoreCircularProgressIndicator()

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = StaticTextId.UiId.CreateProfileText.translate(),
            fontSize = 20.sp,
            color = CoreColors.background.textColor.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorContent(
    state: CreateProfileFinishState.Error,
    onEvent: (CreateProfileFinishEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 30.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Default.WarningAmber,
            contentDescription = null
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = state.text.translate(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        CoreCallToActionButton(
            text = StaticTextId.UiId.TryAgain.translate(),
            onClick = { onEvent(CreateProfileFinishEvent.Retry) }
        )

        TextButton(onClick = { onEvent(CreateProfileFinishEvent.Back) }) {
            Text(
                text = StaticTextId.UiId.BackToProfileEditing.translate(),
                color = CoreColors.background.textColor
            )
        }
    }
}
