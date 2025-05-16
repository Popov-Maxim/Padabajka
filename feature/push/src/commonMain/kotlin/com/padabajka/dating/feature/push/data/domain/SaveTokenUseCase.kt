package com.padabajka.dating.feature.push.data.domain

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging

class SaveTokenUseCase(
    private val updateTokenUseCase: UpdateTokenUseCase,
) {
    suspend operator fun invoke() {
        // TODO(push token): fix sending token to server for ios
        val token = runCatching { Firebase.messaging.getToken() }.onFailure { exception ->
            println("LOG: push failed ${exception.message}")
        }.getOrNull() ?: return
        println("LOG: push getToken $token")
        runCatching {
            updateTokenUseCase.invoke(token)
        }
    }
}
