package com.padabajka.dating.component.room.asset.city.entry

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "city_translations", primaryKeys = ["cityId", "language"])
class CityTranslation(
    val cityId: String,
    val language: String,
    @ColumnInfo("name", collate = ColumnInfo.NOCASE) val name: String
)
