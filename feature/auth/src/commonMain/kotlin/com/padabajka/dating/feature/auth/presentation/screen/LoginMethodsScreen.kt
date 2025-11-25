package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.feature.auth.presentation.LoginMethodsComponent
import com.padabajka.dating.feature.auth.presentation.model.LoginMethodEvent

@Composable
fun LoginMethodsScreen(component: LoginMethodsComponent) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 45.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically),
    ) {
        AuthMethodButton(
            text = "Email",
            icon = CoreIcons.Login.MailLogo.toData(),
            onClick = { component.onEvent(LoginMethodEvent.SelectEmailMethod) }
        )

        AuthMethodButton(
            text = "Google",
            icon = CoreIcons.Login.GoogleLogo.toData(),
            onClick = { component.onEvent(LoginMethodEvent.SelectGoogleMethod) }
        )

        AuthMethodButton(
            text = "Debug Login",
            onClick = { component.onEvent(LoginMethodEvent.SelectDebugMethod) }
        )
    }
}
