package com.padabajka.dating.feature.auth.presentation.screen

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
import com.padabajka.dating.feature.auth.presentation.DebugLoginMethodComponent
import com.padabajka.dating.feature.auth.presentation.model.DebugLoggingInState
import com.padabajka.dating.feature.auth.presentation.model.DebugLoginMethodEvent

@Composable
fun DebugLoginMethodScreen(component: DebugLoginMethodComponent) {
    val state by component.state.subscribeAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 45.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 25.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailFieldEditor(component::onEvent, state)
    }
}

@Composable
private fun EmailFieldEditor(
    onEvent: (DebugLoginMethodEvent) -> Unit,
    state: DebugLoggingInState
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
            text = state.uuid,
            enabled = true,
            onChange = {
                onEvent(DebugLoginMethodEvent.UUIDChanged(it))
            }
        )

        CallToActionButton(
            text = "Login",
            enabled = state.uuid.isNotEmpty(),
            onClick = {
                autofillManager?.commit()
                onEvent(DebugLoginMethodEvent.LoginClick)
            }
        )
    }

    Text(text = "Or")

    AuthMethodButton(
        text = "Other method login",
        onClick = {
            onEvent(DebugLoginMethodEvent.BackToLoginMethods)
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
        modifierAfter = Modifier.padding(5.dp),
        text = text,
        hint = "uuid",
        enabled = enabled,
        onChange = onChange,
        onClick = onClick
    )
}
