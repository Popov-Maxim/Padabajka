package com.padabajka.dating.component.room.messenger.converters

import androidx.room.TypeConverter
import com.padabajka.dating.component.room.messenger.entry.MessageReactionEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessageReactionEntityConverters {

    @TypeConverter
    fun fromString(value: String): List<MessageReactionEntity> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toString(reactions: List<MessageReactionEntity>): String {
        return Json.encodeToString(reactions)
    }
}
