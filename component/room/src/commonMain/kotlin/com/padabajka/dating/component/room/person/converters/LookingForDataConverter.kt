package com.padabajka.dating.component.room.person.converters

import androidx.room.TypeConverter
import com.padabajka.dating.core.data.network.model.DetailDto
import com.padabajka.dating.core.data.network.model.LanguagesAssetDto
import com.padabajka.dating.core.data.network.model.LifestyleDto
import com.padabajka.dating.core.data.network.model.LookingForDataDto
import com.padabajka.dating.core.data.network.model.TextDto
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

    @TypeConverter
    fun fromDetailDto(value: List<DetailDto>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toDetailDto(value: String): List<DetailDto> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromLifestyleDto(value: List<LifestyleDto>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toLifestyleDto(value: String): List<LifestyleDto> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromTextDto(value: List<TextDto>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toTextDto(value: String): List<TextDto> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromLanguagesAssetDto(value: LanguagesAssetDto): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toLanguagesAssetDto(value: String): LanguagesAssetDto {
        return Json.decodeFromString(value)
    }
}
