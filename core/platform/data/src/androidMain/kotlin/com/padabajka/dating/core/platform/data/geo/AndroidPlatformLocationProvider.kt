package com.padabajka.dating.core.platform.data.geo

import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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
    override suspend fun getCurrentLocation(): PlatformLocation? {
        return suspendCancellableCoroutine { cont ->
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    cont.resume(
                        PlatformLocation(
                            lat = loc.latitude,
                            lon = loc.longitude,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                } else {
                    cont.resume(null)
                }
            }.addOnFailureListener { cont.resume(null) }
        }
    }
}
