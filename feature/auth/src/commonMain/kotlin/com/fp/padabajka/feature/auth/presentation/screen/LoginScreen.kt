package com.fp.padabajka.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.core.presentation.ui.TextInputField
import com.fp.padabajka.feature.auth.presentation.LoginComponent
import com.fp.padabajka.feature.auth.presentation.element.AuthButton
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.GoToRegistrationClick
import com.fp.padabajka.feature.auth.presentation.model.LoginClick
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldUpdate

@Composable
fun LoginScreen(component: LoginComponent) {
    val state = component.state.subscribeAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextInputField(
            text = state.value.email,
            hint = "Email",
            isError = state.value.emailValidationIssue != null,
            onChange = { component.onEvent(EmailFieldUpdate(it)) },
            onFocusLost = { component.onEvent(EmailFieldLoosFocus) }
        )
        TextInputField(
            text = state.value.password,
            hint = "Password",
            onChange = { component.onEvent(PasswordFieldUpdate(it)) }
        )
        AuthButton(text = "Login", onClick = { component.onEvent(LoginClick) })
        AuthButton(text = "To registration", onClick = { component.onEvent(GoToRegistrationClick) })
    }
}
