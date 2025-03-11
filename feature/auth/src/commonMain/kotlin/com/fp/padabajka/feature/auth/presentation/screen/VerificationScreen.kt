package com.fp.padabajka.feature.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.auth.presentation.VerificationComponent
import com.fp.padabajka.feature.auth.presentation.model.ContinueVerification
import com.fp.padabajka.feature.auth.presentation.model.ResendState
import com.fp.padabajka.feature.auth.presentation.model.ResendVerification

@Composable
fun VerificationScreen(component: VerificationComponent) {
    val state by component.state.subscribeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(10.dp).align(Alignment.Center)) {
            Text(text = "Verification", fontSize = 50.sp)
            Spacer(Modifier.height(10.dp))
            Text(text = VERIFICATION_TEXT)
            Spacer(Modifier.height(5.dp))
            Text("If you haven't received the email, try checking your spam folder or tap \"Resend\" to send it again.")
            Spacer(Modifier.height(5.dp))
            Text("Once verified, tap \"Continue\" to proceed.")
            Spacer(Modifier.height(30.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                val buttonModifier = Modifier.width(150.dp)
                Button(
                    onClick = {
                        component.onEvent(ResendVerification)
                    },
                    enabled = when (state.resendState) {
                        ResendState.Available -> true
                        is ResendState.Unavailable -> false
                    },
                    modifier = buttonModifier
                ) {
                    val text = when (val resend = state.resendState) {
                        ResendState.Available -> "Resend"
                        is ResendState.Unavailable -> "Resend (${resend.time})"
                    }
                    Text(text)
                }
                Spacer(Modifier.width(50.dp))
                Button(onClick = {
                    component.onEvent(ContinueVerification)
                }, modifier = buttonModifier) {
                    Text("Continue")
                }
            }
        }
    }
}

private const val VERIFICATION_TEXT = "A verification email has been sent to [user's email]. " +
    "Please check your inbox and follow the instructions to verify your account."
