package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.PushMessage
import com.padabajka.dating.feature.push.data.domain.model.MessagePush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class DataPushParser {
    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }
    fun parse(messagePush: MessagePush): PushMessage? {
        val dataPushJson = messagePush.dataJson
        return if (dataPushJson.containsKey("event")) {
            json.decodeFromJsonElement(dataPushJson)
        } else {
            null
        }
    }
}
