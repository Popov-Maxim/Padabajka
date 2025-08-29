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
import com.padabajka.dating.feature.auth.presentation.EmailLoginMethodComponent
import com.padabajka.dating.feature.auth.presentation.model.EmailLoginMethodEvent

@Composable
fun EmailLoginMethodScreen(component: EmailLoginMethodComponent) {
    val state by component.state.subscribeAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 45.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 25.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
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
            CoreTextEditField(
                modifier = Modifier.fillMaxWidth()
                    .semantics {
                        contentType = ContentType.Username
                    },
                modifierAfter = Modifier.padding(5.dp), // TODO: calculate correct padding
                text = state.email,
                hint = "example@email.com",
                onChange = {
                    component.onEvent(EmailLoginMethodEvent.EmailChanged(it))
                },
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
}
