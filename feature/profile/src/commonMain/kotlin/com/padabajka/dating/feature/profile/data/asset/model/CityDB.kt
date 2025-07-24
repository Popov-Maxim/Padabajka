package com.padabajka.dating.feature.profile.data.asset.model

import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.core.repository.api.model.profile.asset.City

data class CityDB(
    val name: String,
    val lat: Double,
    val lon: Double,
    val radiusKm: Double
)

fun CityDB.toDomain(): City {
    return City(
        name = name,
        lat = lat,
        lon = lon,
        radiusKm = radiusKm
    )
}

fun CityEntry.toDomain(name: String): CityDB {
    return CityDB(
        name = name,
        lat = lat,
        lon = lon,
        radiusKm = radiusKm
    )
}
