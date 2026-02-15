package com.padabajka.dating.settings.data.geo.source

import com.padabajka.dating.core.permission.GeoPermissionController
import com.padabajka.dating.core.platform.data.geo.PlatformLocation
import com.padabajka.dating.core.platform.data.geo.PlatformLocationProvider
import com.padabajka.dating.core.repository.api.model.location.Location

class LocalGeoDataSource(
    private val platformLocationProvider: PlatformLocationProvider,
    private val geoPermissionController: GeoPermissionController
) {
    suspend fun location(): Location? {
        return if (geoPermissionController.hasPermission()) {
            platformLocationProvider.getCurrentLocation()
                ?.toDomain()
        } else {
            null
        }
    }
}

fun PlatformLocation.toDomain(): Location {
    return Location(
        lon = lon,
        lat = lat,
        timestamp = timestamp
    )
}
