package com.padabajka.dating.feature.profile.data.asset.model

import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.city.entry.CityTranslation
import kotlinx.serialization.Serializable

@Serializable
class CityDto(
    val id: String,
    val lat: Double,
    val lon: Double,
    val radiusKm: Double,
    val translations: Map<String, String>
)

fun CityDto.toEntity(): CityEntry {
    return CityEntry(
        id = id,
        lat = lat,
        lon = lon,
        radiusKm = radiusKm
    )
}

fun CityDto.toTranslationEntity(): List<CityTranslation> {
    return translations.map { (language, translation) ->
        CityTranslation(
            cityId = id,
            language = language,
            name = translation
        )
    }
}
