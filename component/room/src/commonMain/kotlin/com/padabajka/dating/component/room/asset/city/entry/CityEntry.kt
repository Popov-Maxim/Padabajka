package com.padabajka.dating.component.room.asset.city.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
class CityEntry(
    @PrimaryKey
    val id: String,
    @ColumnInfo("lat") val lat: Double,
    @ColumnInfo("lon") val lon: Double,
    @ColumnInfo("radiusKm") val radiusKm: Double
)
