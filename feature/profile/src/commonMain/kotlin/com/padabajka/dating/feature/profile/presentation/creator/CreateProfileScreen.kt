package com.padabajka.dating.feature.profile.presentation.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor

@Composable
fun CreateProfileScreen(context: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(CoreColors.background.mainColor)) {
        context()
    }
}

@Composable
fun CreateProfileButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CoreCallToActionButton(
        text = text,
        modifier = modifier.padding(horizontal = 35.dp, vertical = 10.dp),
        onClick = onClick
    )
}
