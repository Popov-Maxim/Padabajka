package com.padabajka.dating.feature.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.GhostButton
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.feature.auth.presentation.model.AccountDeletedEvent

@Composable
fun AccountDeletedScreen(textId: StaticTextId, component: AccountDeletedScreenComponent) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxSize()) {
            Text(
                text = textId.translate(),
                fontFamily = PlayfairDisplay,
                fontSize = 22.sp,
                lineHeight = 1.33.em,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 45.dp)
            )

            GhostButton(
                text = StaticTextId.UiId.LogOut.translate(),
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(horizontal = 35.dp, vertical = 10.dp),
                onClick = { component.onEvent(AccountDeletedEvent.Logout) }
            )
        }
    }
}
