package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SharedDeeplinkHandler : KoinComponent {

    private val authRepository: AuthRepository by inject()

    private val scope: CoroutineScope by inject()

    fun handle(uri: String) {
        scope.launch {
            runCatching {
                if (uri.startsWith("https://padabajka-96c95.firebaseapp.com/__/auth/links")) {
                    authRepository.signInWithEmailLink(uri)
                }
            }.onFailure {
                println("SharedDeeplinkHandler: FAILED signInWithEmailLink")
            }.onSuccess {
                println("SharedDeeplinkHandler: SUCCESS signInWithEmailLink")
            }
        }
    }
}
