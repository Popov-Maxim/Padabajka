package com.padabajka.dating.core.platform.data.geo

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class AndroidPlatformLocationProvider(
    context: Context
) : PlatformLocationProvider {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @androidx.annotation.RequiresPermission(
        allOf = [
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    override suspend fun cachedLocation(): PlatformLocation? {
        val location = runCatching { fusedClient.lastLocation.await() }.getOrNull()
        return location?.toPlatform()
    }

    override suspend fun requestCurrentLocation(): PlatformLocation? {
        val location = fusedClient
            .getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null,
            )
            .await()

        return location?.toPlatform()
    }

    private fun Location.toPlatform(): PlatformLocation {
        return PlatformLocation(
            lat = this.latitude,
            lon = this.longitude,
            timestamp = this.time
        )
    }
}
