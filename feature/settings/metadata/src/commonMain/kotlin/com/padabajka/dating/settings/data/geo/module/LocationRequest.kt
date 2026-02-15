package com.padabajka.dating.settings.data.geo.module

import com.padabajka.dating.core.repository.api.model.location.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationRequest(
    val lon: Double,
    val lat: Double,
    val timestamp: Long
)

fun Location.toRequest(): LocationRequest {
    return LocationRequest(
        lon = lon,
        lat = lat,
        timestamp = timestamp
    )
}
