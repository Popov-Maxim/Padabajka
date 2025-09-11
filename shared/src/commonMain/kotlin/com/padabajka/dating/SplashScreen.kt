package com.padabajka.dating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreCircularProgressIndicator

@Composable
fun SplashScreen(
    loadingMessage: String
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp, alignment = Alignment.CenterVertically)
    ) {
        CoreCircularProgressIndicator()
        Text(
            text = loadingMessage,
            fontSize = 20.sp
        )
    }
}
