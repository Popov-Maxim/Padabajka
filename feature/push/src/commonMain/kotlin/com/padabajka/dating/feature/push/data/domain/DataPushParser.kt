package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.feature.push.data.domain.model.DataPush
import com.padabajka.dating.feature.push.data.domain.model.PlatformMessagePush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class DataPushParser {
    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }
    fun parse(platformMessagePush: PlatformMessagePush): DataPush? {
        val dataPushJson = platformMessagePush.dataJson
        return if (dataPushJson.containsKey("type")) {
            json.decodeFromJsonElement(dataPushJson)
        } else {
            null
        }
    }
}
