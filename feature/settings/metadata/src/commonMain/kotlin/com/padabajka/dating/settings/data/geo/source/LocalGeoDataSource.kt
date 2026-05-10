package com.padabajka.dating.settings.data.geo.source

import com.padabajka.dating.core.permission.GeoPermissionController
import com.padabajka.dating.core.platform.data.geo.PlatformLocation
import com.padabajka.dating.core.platform.data.geo.PlatformLocationProvider
import com.padabajka.dating.core.repository.api.exception.GeoExceptions
import com.padabajka.dating.core.repository.api.model.location.Location
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class LocalGeoDataSource(
    private val platformLocationProvider: PlatformLocationProvider,
    private val geoPermissionController: GeoPermissionController
) {
    suspend fun location(): Location {
        return if (geoPermissionController.hasPermission()) {
            val cashLoc = platformLocationProvider.cachedLocation()
            val loc = cashLoc?.takeIf { it.valid() }
                ?: withTimeoutOrNull(timeoutForRequest(cashLoc)) {
                    platformLocationProvider.requestCurrentLocation()
                }
                ?: cashLoc

            loc?.toDomain() ?: throw GeoExceptions.UnknownError()
        } else {
            throw GeoExceptions.HasNotPermission()
        }
    }

    private fun PlatformLocation.valid(): Boolean {
        val locationTime = Instant.fromEpochMilliseconds(this.timestamp)
        val now = Clock.System.now()

        return (now - locationTime) < 5.minutes
    }

    private fun timeoutForRequest(cashLoc: PlatformLocation?): Duration {
        return if (cashLoc != null) 5.seconds else 10.seconds
    }
}

private fun PlatformLocation.toDomain(): Location {
    return Location(
        lon = lon,
        lat = lat,
        timestamp = Clock.System.now().toEpochMilliseconds() // timestamp
    )
}
