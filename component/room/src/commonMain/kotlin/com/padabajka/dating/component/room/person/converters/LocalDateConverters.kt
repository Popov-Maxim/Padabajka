package com.padabajka.dating.component.room.person.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverters {
    @TypeConverter
    fun fromLocalDate(value: LocalDate): Int {
        return value.toEpochDays()
    }

    @TypeConverter
    fun toLocalDate(value: Int): LocalDate {
        return LocalDate.fromEpochDays(value)
    }
}
