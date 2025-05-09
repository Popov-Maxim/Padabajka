package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.TextInputField
import com.padabajka.dating.feature.auth.presentation.RegisterComponent
import com.padabajka.dating.feature.auth.presentation.element.AuthButton
import com.padabajka.dating.feature.auth.presentation.model.EmailFieldLoosFocus
import com.padabajka.dating.feature.auth.presentation.model.EmailFieldUpdate
import com.padabajka.dating.feature.auth.presentation.model.GoToLoginClick
import com.padabajka.dating.feature.auth.presentation.model.PasswordFieldLoosFocus
import com.padabajka.dating.feature.auth.presentation.model.PasswordFieldUpdate
import com.padabajka.dating.feature.auth.presentation.model.PasswordValidationIssue
import com.padabajka.dating.feature.auth.presentation.model.RegisterClick
import com.padabajka.dating.feature.auth.presentation.model.RepeatedPasswordFieldLoosFocus
import com.padabajka.dating.feature.auth.presentation.model.RepeatedPasswordFieldUpdate

@Composable
fun RegisterScreen(component: RegisterComponent) {
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
            isError = state.value.passwordValidationIssue != null,
            onChange = { component.onEvent(PasswordFieldUpdate(it)) },
            onFocusLost = { component.onEvent(PasswordFieldLoosFocus) }
        )
        TextInputField(
            text = state.value.repeatedPassword,
            hint = "Repeat password",
            isError = state.value.passwordValidationIssue == PasswordValidationIssue.PasswordsNotMatching,
            onChange = { component.onEvent(RepeatedPasswordFieldUpdate(it)) },
            onFocusLost = { component.onEvent(RepeatedPasswordFieldLoosFocus) }
        )
        AuthButton(text = "Register", onClick = { component.onEvent(RegisterClick) })
        AuthButton(text = "To login", onClick = { component.onEvent(GoToLoginClick) })
    }
}
