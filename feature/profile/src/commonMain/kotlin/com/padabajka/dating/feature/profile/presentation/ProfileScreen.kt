package com.padabajka.dating.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.ProfileAvatar
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.repository.api.model.profile.age
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.profile.presentation.model.OpenEditorEvent
import com.padabajka.dating.feature.profile.presentation.model.ProfileValue
import com.padabajka.dating.feature.profile.presentation.model.UpdateProfileEvent

@Composable
fun ProfileScreen(
    component: ProfileScreenComponent,
    navigateBar: @Composable () -> Unit,
    openSettings: () -> Unit
) {
    val state by component.state.subscribeAsState()

    CustomScaffold(
        bottomBar = navigateBar,
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = StaticTextId.UiId.Profile.translate(),
                    fontFamily = PlayfairDisplay,
                    fontSize = 30.sp
                )
                IconButton(
                    onClick = openSettings,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "settings"
                    )
                }
            }
        }
    ) {
        when (val profile = state.value) {
            ProfileValue.Loading -> LoadingScreen()
            is ProfileValue.Loaded -> ProfileScreen(component, profile)
            ProfileValue.Error -> ErrorScreen(component)
        }
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
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
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
    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        Box(
            modifier = Modifier
                .innerShadow(
                    color = Color(color = 0xFFA1A1A1),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .padding(vertical = 20.dp, horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    ProfileAvatar(
                        model = profile.images.firstOrNull()?.raw(),
                        modifier = Modifier.size(100.dp)
                    )
                    Column(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val nameWithAge = "${profile.name}, ${profile.birthday.age.raw}"
                        Text(
                            text = nameWithAge,
                            fontSize = 26.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(color = CoreColors.secondary.mainColor, shape = RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
                                text = profile.lookingFor.type.translate(),
                                fontSize = 15.sp,
                                color = CoreColors.secondary.textColor
                            )
                        }
                    }
                }
                Button(
                    onClick = { component.onEvent(OpenEditorEvent) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(StaticTextId.UiId.OpenProfileEditor.translate())
                }
            }
        }
    }
}
