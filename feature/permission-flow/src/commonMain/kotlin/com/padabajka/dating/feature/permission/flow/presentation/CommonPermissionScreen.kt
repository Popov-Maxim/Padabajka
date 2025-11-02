package com.padabajka.dating.feature.permission.flow.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.GhostButton
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay

@Composable
fun CommonPermissionScreen(
    message: String,
    onApply: () -> Unit,
    onSkip: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 40.dp)
        ) {
            Text(
                text = message,
                fontSize = 25.sp,
                fontFamily = PlayfairDisplay,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            GhostButton(
                text = "Skip",
                onClick = onSkip
            )

            CoreCallToActionButton(
                text = StaticTextId.UiId.Apply.translate(),
                onClick = onApply
            )
        }
    }
}
