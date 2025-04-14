package com.padabajka.dating.component.room.person.converters

import androidx.room.TypeConverter
import com.padabajka.dating.core.data.network.model.LookingForDataDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LookingForDataConverter {
    @TypeConverter
    fun fromLookingForData(value: LookingForDataDto): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toLookingForData(value: String): LookingForDataDto {
        return Json.decodeFromString(value)
    }
}
