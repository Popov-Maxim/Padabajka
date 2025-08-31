package com.padabajka.dating.feature.profile.presentation.creator.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileButton
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen

@Composable
fun WelcomeScreen(toNext: () -> Unit) {
    CreateProfileScreen {
        Box(Modifier.fillMaxSize()) {
            Text(
                text = StaticTextId.UiId.WelcomeText.translate(),
                fontFamily = PlayfairDisplay,
                fontSize = 22.sp,
                lineHeight = (22 * 1.33).sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 45.dp)
            )

            CreateProfileButton(
                text = StaticTextId.UiId.CreateProfileBtn.translate(),
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = toNext
            )
        }
    }
}
