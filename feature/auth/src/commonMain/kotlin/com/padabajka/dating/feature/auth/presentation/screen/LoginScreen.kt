package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.TextInputField
import com.padabajka.dating.feature.auth.presentation.LoginComponent
import com.padabajka.dating.feature.auth.presentation.element.AuthButton
import com.padabajka.dating.feature.auth.presentation.model.EmailFieldLoosFocus
import com.padabajka.dating.feature.auth.presentation.model.EmailFieldUpdate
import com.padabajka.dating.feature.auth.presentation.model.GoToRegistrationClick
import com.padabajka.dating.feature.auth.presentation.model.LoginClick
import com.padabajka.dating.feature.auth.presentation.model.PasswordFieldUpdate

@Composable
fun LoginScreen(component: LoginComponent) {
    val state = component.state.subscribeAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val autofillManager = LocalAutofillManager.current
        TextInputField(
            modifier = Modifier
                .semantics {
                    contentType = ContentType.Username
                },
            text = state.value.email,
            hint = "Email",
            isError = state.value.emailValidationIssue != null,
            onChange = { component.onEvent(EmailFieldUpdate(it)) },
            onFocusLost = { component.onEvent(EmailFieldLoosFocus) }
        )
        TextInputField(
            modifier = Modifier.semantics {
                contentType = ContentType.Password
            },
            text = state.value.password,
            hint = "Password",
            onChange = { component.onEvent(PasswordFieldUpdate(it)) }
        )
        AuthButton(
            text = "Login",
            onClick = {
                autofillManager?.commit()
                component.onEvent(LoginClick)
            }
        )
        AuthButton(text = "To registration", onClick = { component.onEvent(GoToRegistrationClick) })
    }
}
