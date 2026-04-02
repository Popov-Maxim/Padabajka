package com.padabajka.dating.feature.subscription.presentation

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionEvent
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionScreenState

@Composable
fun SubscriptionScreen(component: SubscriptionScreenComponent) {
    val state by component.state.subscribeAsState()

    Content(
        component = component,
        state = state
    )
}

@Composable
private fun Content(
    component: SubscriptionScreenComponent,
    state: SubscriptionScreenState,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.monthSub.price,
                fontSize = 30.sp
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            CoreCallToActionButton(
                text = StaticTextId.UiId.Apply.translate(),
                onClick = {
                    component.onEvent(SubscriptionEvent.Apply)
                }
            )
        }
    }
}
