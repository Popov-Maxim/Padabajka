package com.fp.padabajka.component.room.messenger.converters

import androidx.room.TypeConverter
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction

class MessageReactionConverters {

    @TypeConverter
    fun fromString(value: String): MessageReaction {
        return MessageReaction.valueOf(value)
    }

    @TypeConverter
    fun toString(messageReaction: MessageReaction): String {
        return messageReaction.name
    }
}
