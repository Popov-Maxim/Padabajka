package com.padabajka.dating.settings.data.geo

import com.padabajka.dating.core.repository.api.GeoRepository
import com.padabajka.dating.core.repository.api.model.location.Location
import com.padabajka.dating.settings.data.geo.module.toRequest
import com.padabajka.dating.settings.data.geo.source.LocalGeoDataSource
import com.padabajka.dating.settings.data.geo.source.RemoteGeoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GeoRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val localGeoDataSource: LocalGeoDataSource,
    private val remoteGeoDataSource: RemoteGeoDataSource
) : GeoRepository {

    private var lastSentLocation: Location? = null

    private val _locationState: StateFlow<Location?> by lazy {
        geoFlow().stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    }
    override val location: Flow<Location> =
        _locationState.filterNotNull()

    override suspend fun sendLocation(location: Location) {
        if (shouldUpdateLocation(lastSentLocation, location).not()) return

        val request = location.toRequest()
        remoteGeoDataSource.sendLocation(request)
        lastSentLocation = location
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

    private fun geoFlow(): Flow<Location> = flow {
        var lastLocation: Location? = null

        while (currentCoroutineContext().isActive) {
            val loc = localGeoDataSource.location()
            if (loc != null) {
                val distance = lastLocation?.let { haversine(it, loc) } ?: Double.MAX_VALUE
                if (distance >= DISTANCE_THRESHOLD_M) {
                    emit(loc)
                    lastLocation = loc
                }
            }
            delay(INTERVAL_FOR_UPDATE_MS)
        }
    }.distinctUntilChanged()

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
        private const val INTERVAL_FOR_UPDATE_MS = 5000L
        private const val RADIUS_OF_EARTH_M = 6371000.0
        private const val DISTANCE_THRESHOLD_M = 500.0
    }
}
