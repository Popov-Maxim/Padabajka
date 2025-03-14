package com.fp.padabajka.feature.profile.presentation

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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.profile.presentation.model.LogoutEvent
import com.fp.padabajka.feature.profile.presentation.model.OpenEditorEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileValue
import com.fp.padabajka.feature.profile.presentation.model.UpdateProfileEvent

@Composable
fun ProfileScreen(component: ProfileScreenComponent) {
    val state by component.state.subscribeAsState()

    when (val profile = state.value) {
        ProfileValue.Loading -> LoadingScreen()
        is ProfileValue.Loaded -> ProfileScreen(component, profile)
        ProfileValue.Error -> ErrorScreen(component)
    }
}

@Composable
private fun ErrorScreen(component: ProfileScreenComponent) {
    Box(modifier = Modifier.fillMaxSize()) {
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
            LogoutButton {
                component.onEvent(LogoutEvent)
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp).align(Alignment.Center)
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
                .clip(RoundedCornerShape(20.dp)).background(Color.White),
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
        LogoutButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            component.onEvent(LogoutEvent)
        }
    }
}

@Composable
fun LogoutButton(modifier: Modifier = Modifier, onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        modifier = modifier
    ) {
        Text("Logout", color = Color.White)
    }
}
