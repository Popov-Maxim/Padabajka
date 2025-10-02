package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreTextEditField
import com.padabajka.dating.feature.auth.presentation.EmailLoginMethodComponent
import com.padabajka.dating.feature.auth.presentation.model.EmailLoginMethodEvent
import com.padabajka.dating.feature.auth.presentation.model.LoggingInState
import com.padabajka.dating.feature.auth.presentation.model.email

@Composable
fun EmailLoginMethodScreen(component: EmailLoginMethodComponent) {
    val state by component.state.subscribeAsState()

    val emailFieldState = state.emailFieldState
    AnimatedContent(
        targetState = emailFieldState::class,
        transitionSpec = {
            val i = when (emailFieldState) {
                is LoggingInState.FieldState.WaitSignFromEmail -> 1
                is LoggingInState.FieldState.Editor -> -1
            }
            slideInHorizontally(
                initialOffsetX = { i * it }
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -i * it }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 45.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 25.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (emailFieldState) {
                is LoggingInState.FieldState.Editor -> EmailFieldEditor(component, emailFieldState)
                is LoggingInState.FieldState.WaitSignFromEmail -> EmailWaitLogin(
                    component,
                    emailFieldState
                )
            }
        }
    }
}

@Composable
private fun EmailFieldEditor(
    component: EmailLoginMethodComponent,
    state: LoggingInState.FieldState.Editor
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val autofillManager = LocalAutofillManager.current
        EmailField(
            text = state.email(),
            enabled = true,
            onChange = {
                component.onEvent(EmailLoginMethodEvent.EmailChanged(it))
            }
        )

        CallToActionButton(
            text = "Login",
            onClick = {
                autofillManager?.commit()
                component.onEvent(EmailLoginMethodEvent.LoginClick)
            }
        )
    }

    Text(text = "Or")

    AuthMethodButton(
        text = "Other method login",
        onClick = {
            component.onEvent(EmailLoginMethodEvent.BackToLoginMethods)
        }
    )
}

@Composable
private fun EmailWaitLogin(
    component: EmailLoginMethodComponent,
    state: LoggingInState.FieldState.WaitSignFromEmail
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            text = state.email(),
            enabled = false,
            onClick = {
                component.onEvent(EmailLoginMethodEvent.ChangeEmail)
            }
        )

        CallToActionButton(
            text = "Open email app",
            onClick = {
                component.onEvent(EmailLoginMethodEvent.OpenEmailApp)
            }
        )
    }

    Text(text = "Or")

    AuthMethodButton(
        text = "Send link again",
        onClick = {
            component.onEvent(EmailLoginMethodEvent.LoginClick)
        }
    )
}

@Composable
private fun EmailField(
    text: String,
    enabled: Boolean,
    onChange: (String) -> Unit = {},
    onClick: () -> Unit = {}
) {
    CoreTextEditField(
        modifier = Modifier.fillMaxWidth()
            .semantics {
                contentType = ContentType.Username
            },
        modifierAfter = Modifier.padding(5.dp), // TODO: calculate correct padding
        text = text,
        hint = "example@email.com",
        enabled = enabled,
        onChange = onChange,
        onClick = onClick
    )
}
