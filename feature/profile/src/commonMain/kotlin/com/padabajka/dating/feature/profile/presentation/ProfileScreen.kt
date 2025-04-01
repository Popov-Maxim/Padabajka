package com.padabajka.dating.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.profile.presentation.model.LogoutEvent
import com.padabajka.dating.feature.profile.presentation.model.OpenEditorEvent
import com.padabajka.dating.feature.profile.presentation.model.ProfileValue
import com.padabajka.dating.feature.profile.presentation.model.UpdateProfileEvent
import com.padabajka.dating.feature.profile.presentation.setting.AppSettingsDialog

@Composable
fun ProfileScreen(component: ProfileScreenComponent, navigateBar: @Composable () -> Unit) {
    val state by component.state.subscribeAsState()

    CustomScaffold(
        bottomBar = navigateBar,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                when (val profile = state.value) {
                    ProfileValue.Loading -> LoadingScreen()
                    is ProfileValue.Loaded -> ProfileScreen(component, profile)
                    ProfileValue.Error -> ErrorScreen(component)
                }
                LogoutButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    component.onEvent(LogoutEvent)
                }

                AppSettingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen(component: ProfileScreenComponent) {
    Box(modifier = Modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center).wrapContentWidth(),
        ) {
            Text(
                text = "ERROR",
                fontSize = 100.sp
            )
            Button(
                modifier = Modifier,
                onClick = {
                    component.onEvent(UpdateProfileEvent)
                }
            ) {
                Text(
                    text = "Update profile",
                    fontSize = 25.sp
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier) {
        CircularProgressIndicator(
            modifier = Modifier.size(140.dp).align(Alignment.Center)
        )
    }
}

@Composable
private fun ProfileScreen(
    component: ProfileScreenComponent,
    profile: ProfileValue.Loaded
) {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                    Box(modifier = Modifier.size(100.dp).background(Color.LightGray))
                    Column(modifier = Modifier.padding(start = 20.dp)) {
                        Text(
                            text = profile.firstName,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = profile.lastName,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Button(
                onClick = { component.onEvent(OpenEditorEvent) },
                modifier = Modifier.fillMaxWidth().padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                )
            ) {
                Text("Open Profile Editor")
            }
        }
    }
}

@Composable
private fun LogoutButton(modifier: Modifier = Modifier, onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        modifier = modifier
    ) {
        Text("Logout", color = Color.White)
    }
}

@Composable
private fun AppSettingButton(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(
            containerColor = CoreColors.secondary.mainColor,
            contentColor = CoreColors.secondary.textColor,
        ),
        modifier = modifier
    ) {
        Text("AppSettings")
    }

    if (showDialog) {
        AppSettingsDialog { showDialog = false }
    }
}
