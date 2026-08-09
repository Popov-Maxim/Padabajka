package com.padabajka.dating.datapush

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.UpdateTokenUseCase
import com.padabajka.dating.feature.push.data.domain.model.PlatformDataPush
import com.padabajka.dating.session.UserSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object SharedPushHandler : KoinComponent {

    private val updateTokenUseCase: UpdateTokenUseCase
        get() = get()

    private val handlePushUseCase: HandlePushUseCase
        get() = get()

    private val authRepository: AuthRepository
        get() = get()

    private val userSessionManager: UserSessionManager
        get() = get()

    private val scope: CoroutineScope = get()

    fun handlePush(rawPush: PlatformDataPush) {
        scope.launch {
            val userId = authRepository.currentAuthState.userIdOrNull() ?: return@launch
            userSessionManager.runIfActive(userId) {
                handlePushUseCase(rawPush)
            }
        }
    }

    fun saveToken(token: String) {
        println("LOG: push saveToken $token")
        scope.launch {
            runCatching {
                updateTokenUseCase.invoke()
            }.onFailure {
                println("TODO: not impl for error SaveTokenUseCase")
            }
        }
    }
}
