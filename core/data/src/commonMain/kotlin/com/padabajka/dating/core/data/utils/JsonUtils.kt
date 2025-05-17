package com.padabajka.dating.core.data.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

fun Json.mapToJson(map: Map<String, Any?>): JsonObject {
    val jsonElement = anyToJsonElement(map)
    return jsonElement.jsonObject
}

private fun anyToJsonElement(value: Any?): JsonElement = when (value) {
    null -> JsonNull
    is Map<*, *> -> buildJsonObject {
        value.forEach { (k, v) ->
            put(k.toString(), anyToJsonElement(v))
        }
    }

    is List<*> -> buildJsonArray {
        value.forEach { item ->
            add(anyToJsonElement(item))
        }
    }

    is Boolean -> JsonPrimitive(value)
    is Number -> JsonPrimitive(value)
    is String -> Json.parseToJsonElement(value)
    else -> Json.parseToJsonElement(value.toString())
}
