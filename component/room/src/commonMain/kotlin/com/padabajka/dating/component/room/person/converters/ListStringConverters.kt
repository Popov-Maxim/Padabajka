package com.padabajka.dating.component.room.person.converters

import androidx.room.TypeConverter

class ListStringConverters {

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(
            ",",
            prefix = "[",
            postfix = "]",
        )
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return data
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
    }
}
