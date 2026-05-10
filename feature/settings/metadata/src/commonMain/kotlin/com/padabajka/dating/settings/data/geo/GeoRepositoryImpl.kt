package com.padabajka.dating.settings.data.geo

import com.padabajka.dating.core.repository.api.GeoRepository
import com.padabajka.dating.core.repository.api.model.location.Location
import com.padabajka.dating.settings.data.geo.module.toRequest
import com.padabajka.dating.settings.data.geo.source.LocalGeoDataSource
import com.padabajka.dating.settings.data.geo.source.RemoteGeoDataSource
import kotlinx.datetime.Clock
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.minutes

class GeoRepositoryImpl(
    private val localGeoDataSource: LocalGeoDataSource,
    private val remoteGeoDataSource: RemoteGeoDataSource
) : GeoRepository {

    private var lastSentLocation: Location? = null

    override suspend fun location(): Location {
        val now = Clock.System.now().toEpochMilliseconds()
        val intervalWithLast = now - (lastSentLocation?.timestamp ?: now)
        return lastSentLocation
            .takeIf { intervalWithLast < INTERVAL_FOR_UPDATE_MS }
            ?: localGeoDataSource.location()
    }

    override suspend fun sendLocation(location: Location) {
        if (shouldUpdateLocation(lastSentLocation, location).not()) return

        val request = location.toRequest()
        remoteGeoDataSource.sendLocation(request)
        lastSentLocation = location
    }

    override suspend fun sendLocation() {
        val loc = location()
        sendLocation(loc)
    }

    private fun shouldUpdateLocation(
        last: Location?,
        current: Location,
    ): Boolean {
        return when (last) {
            null -> true
            current -> false

            else -> {
                val distance = haversine(last, current)
                val timeDiff = current.timestamp - last.timestamp

                distance >= DISTANCE_THRESHOLD_M || timeDiff >= INTERVAL_FOR_UPDATE_MS
            }
        }
    }

    private fun haversine(a: Location, b: Location): Double {
        val dLat = (b.lat - a.lat).toRadians()
        val dLon = (b.lon - a.lon).toRadians()
        val lat1 = a.lat.toRadians()
        val lat2 = b.lat.toRadians()

        val sinDLat = sin(dLat / 2)
        val sinDLon = sin(dLon / 2)
        val c = 2 * atan2(
            sqrt(sinDLat * sinDLat + cos(lat1) * cos(lat2) * sinDLon * sinDLon),
            sqrt(1 - sinDLat * sinDLat - cos(lat1) * cos(lat2) * sinDLon * sinDLon)
        )
        return RADIUS_OF_EARTH_M * c
    }

    private fun Double.toRadians() = this * DEGREE_TO_RAD

    companion object {
        private const val DEGREE_TO_RAD = PI / 180.0
        private val INTERVAL_FOR_UPDATE_MS = 1.minutes.inWholeMilliseconds
        private const val RADIUS_OF_EARTH_M = 6371000.0
        private const val DISTANCE_THRESHOLD_M = 500.0
    }
}
