package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.feature.push.data.domain.model.MessagePush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class DataPushParser {
    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }
    fun parse(messagePush: MessagePush): DataPush? {
        val dataPushJson = messagePush.dataJson
        return if (dataPushJson.containsKey("type")) {
            json.decodeFromJsonElement(dataPushJson)
        } else {
            null
        }
    }
}
