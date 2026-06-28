package com.padabajka.dating.component.room.messenger.converters

import androidx.room.TypeConverter
import com.padabajka.dating.component.room.messenger.entry.MessageContentDB
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessageContentConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @TypeConverter
    fun fromString(value: String): MessageContentDB {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun toString(messageContent: MessageContentDB): String {
        return json.encodeToString(messageContent)
    }
}
