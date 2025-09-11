package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.BottomWaveShape
import com.padabajka.dating.core.presentation.ui.modifier.Gradient
import com.padabajka.dating.settings.presentation.setting.AppSettingsDialog

@Composable
fun LoginScreen(context: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(CoreColors.background.mainColor)) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
                .shadow(
                    elevation = 10.dp,
                    ambientColor = Color.Black,
                    spotColor = Color.Black,
                    shape = BottomWaveShape
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = Gradient.colorsForLoginScreen,
                        start = Gradient.rightTopOffset,
                        end = Gradient.leftBottomOffset,
                    ),
                    shape = BottomWaveShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            Image(
                modifier = Modifier.width(200.dp),
                painter = CoreIcons.AppLogo,
                contentDescription = "App logo",
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "PADABAJKA",
                letterSpacing = 50.sp / 10,
                fontSize = 50.sp,
                fontFamily = PlayfairDisplay,
                fontWeight = FontWeight.Bold,
                color = CoreColors.Login.appNameColor,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().weight(weight = 1.618f),
        ) {
            SettingButton(
                modifier = Modifier.align(Alignment.TopEnd)
            )
            context()
        }
    }
}

@Composable
private fun SettingButton(modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showDialog = true
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "settings"
        )
    }

    if (showDialog) {
        AppSettingsDialog { showDialog = false }
    }
}
