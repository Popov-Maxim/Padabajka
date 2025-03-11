package com.fp.padabajka

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fp.padabajka.core.repository.api.model.auth.LoggedIn
import com.fp.padabajka.core.repository.api.model.auth.LoggedOut
import com.fp.padabajka.core.repository.api.model.auth.WaitingForEmailValidation
import com.fp.padabajka.feature.auth.domain.AuthStateProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Composable
fun rememberAuthStateObserver(
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    onLoginWithoutVerification: () -> Unit
) = remember {
    AuthStateObserver(
        onLogin = onLogin,
        onLogout = onLogout,
        onLoginWithoutVerification = onLoginWithoutVerification
    )
}

class AuthStateObserver(
    private val onLogin: () -> Unit,
    private val onLogout: () -> Unit,
    private val onLoginWithoutVerification: () -> Unit
) : KoinComponent {

    private val authProvider: AuthStateProvider = get()

    suspend fun subscribeToAuth() {
        authProvider.authState.collect {
            when (it) {
                is LoggedIn -> onLogin()
                LoggedOut -> onLogout()
                is WaitingForEmailValidation -> onLoginWithoutVerification()
            }
        }
    }
}
