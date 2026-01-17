package com.padabajka.dating.feature.profile.data.asset.model

import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry
import com.padabajka.dating.core.repository.api.model.profile.Text
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

fun CityDto.toTranslationEntity(): List<AssetsTranslationEntry> {
    return translations.map { (language, translation) ->
        AssetsTranslationEntry(
            id = id,
            type = Text.Type.City.raw,
            language = language,
            name = translation
        )
    }
}
