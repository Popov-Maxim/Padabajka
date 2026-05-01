package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.SimpleConfirmDialog
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.utils.isDebugBuild
import com.padabajka.dating.feature.auth.presentation.LoginMethodsComponent
import com.padabajka.dating.feature.auth.presentation.model.LoginMethodEvent
import com.padabajka.dating.feature.auth.presentation.model.LoginSingleEvent

@Composable
fun LoginMethodsScreen(component: LoginMethodsComponent) {
    var showDialogWithText: StaticTextId? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        component.event.collect {
            when (it) {
                is LoginSingleEvent.ShowDialog -> {
                    showDialogWithText = it.text
                }
            }
        }
    }
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

        if (isDebugBuild) {
            AuthMethodButton(
                text = "Debug Login",
                onClick = { component.onEvent(LoginMethodEvent.SelectDebugMethod) }
            )
        }
    }

    showDialogWithText?.let {
        SimpleConfirmDialog(
            text = it.translate(),
            confirmText = "OK",
            onConfirm = { showDialogWithText = null },
            dismissText = null,
            onDismiss = { showDialogWithText = null },
            onDismissRequest = { showDialogWithText = null }
        )
    }
}
