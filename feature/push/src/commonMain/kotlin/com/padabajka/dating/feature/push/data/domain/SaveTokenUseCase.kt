package com.padabajka.dating.feature.push.data.domain

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging

class SaveTokenUseCase(
    private val updateTokenUseCase: UpdateTokenUseCase,
) {
    suspend operator fun invoke() {
        val token = Firebase.messaging.getToken()
        println("LOG: push getToken $token")
        updateTokenUseCase.invoke(token)
    }
}
