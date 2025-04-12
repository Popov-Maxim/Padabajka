package com.padabajka.dating.feature.messenger.presentation.element

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.feature.messenger.presentation.model.item.MessengerItem

@Composable
fun MessengerSpacer(first: MessengerItem, second: MessengerItem) {
    val size = when {
        first::class == second::class -> 8.dp
        else -> 18.dp
    }
    Spacer(Modifier.height(size))
}
