package com.fp.padabajka.component.room.messenger.converters

import androidx.room.TypeConverter
import com.fp.padabajka.core.repository.api.model.messenger.MessageStatus

class MessageStatusConverters {

    @TypeConverter
    fun fromString(value: String): MessageStatus {
        return MessageStatus.valueOf(value)
    }

    @TypeConverter
    fun toString(value: MessageStatus): String {
        return value.name
    }
}
