package com.padabajka.dating.feature.push.socket.domain

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.model.MessagePush
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class SocketMessageObserver(
    private val coroutineScope: CoroutineScope,
    private val socketRepository: SocketRepository,
    private val handlePushUseCase: HandlePushUseCase
) {
    fun subscribe() {
        coroutineScope.launch {
            socketRepository.message.collect { value ->
                val json = Json.decodeFromString<JsonObject>(value)
                val messagePush = MessagePush.Json(json)
                handlePushUseCase.invoke(messagePush)
            }
        }
        coroutineScope.launch {
            socketRepository.startConnecting()
        }
    }

    suspend fun unsubscribe() {
        socketRepository.disconnect()
    }
}
