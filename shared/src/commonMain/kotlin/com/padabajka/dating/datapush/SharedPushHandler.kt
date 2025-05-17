package com.padabajka.dating.datapush

import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.SaveTokenUseCase
import com.padabajka.dating.feature.push.data.domain.UpdateTokenUseCase
import com.padabajka.dating.feature.push.data.domain.model.PlatformMessagePush
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object SharedPushHandler : KoinComponent {

    private val updateTokenUseCase: UpdateTokenUseCase
        get() = get()

    private val saveTokenUseCase: SaveTokenUseCase
        get() = get()

    private val handlePushUseCase: HandlePushUseCase
        get() = get()

    private val scope: CoroutineScope = get()

    fun handlePush(rawPush: PlatformMessagePush) {
        scope.launch {
            handlePushUseCase(rawPush)
        }
    }

    fun saveToken(token: String) {
        println("LOG: push saveToken $token")
        scope.launch {
            updateTokenUseCase(token)
        }
    }

    fun saveLocalToken() {
        println("LOG: push saveLocalToken")
        scope.launch {
            saveTokenUseCase()
        }
    }
}
