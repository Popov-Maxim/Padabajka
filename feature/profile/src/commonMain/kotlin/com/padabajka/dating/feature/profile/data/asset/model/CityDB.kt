package com.padabajka.dating.feature.profile.data.asset.model

import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.core.repository.api.model.profile.asset.City

data class CityDB(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val radiusKm: Double
)

fun CityDB.toDomain(): City {
    return City(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        radiusKm = radiusKm
    )
}

fun CityEntry.toDB(name: String): CityDB {
    return CityDB(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        radiusKm = radiusKm
    )
}

fun CityEntry.toDomain(name: String): City {
    return City(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        radiusKm = radiusKm
    )
}
