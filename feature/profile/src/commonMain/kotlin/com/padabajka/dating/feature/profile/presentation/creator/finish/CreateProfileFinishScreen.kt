package com.padabajka.dating.feature.profile.presentation.creator.finish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreCircularProgressIndicator
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.profile.presentation.creator.CreateProfileScreen

@Composable
@Suppress("UnusedParameter")
fun CreateProfileFinishScreen(component: CreateProfileFinishScreenComponent) {
    CreateProfileScreen {
        val spaceBetween = 50.dp
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 45.dp)
                .padding(top = spaceBetween), // TODO: move progressbar in center
            verticalArrangement = Arrangement.spacedBy(
                space = spaceBetween,
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
}
