package com.padabajka.dating.core.platform.data.geo

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.Foundation.timeIntervalSince1970
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IOSLocationProvider : PlatformLocationProvider {

    private val locationManager by lazy {
        CLLocationManager().apply {
            desiredAccuracy = kCLLocationAccuracyBest
            requestWhenInUseAuthorization()
        }
    }
    private var locationDelegate: CLLocationManagerDelegateProtocol? = null

    override suspend fun cachedLocation(): PlatformLocation? = withContext(Dispatchers.Main.immediate) {
        locationManager.location?.toPlatform()
    }

    override suspend fun requestCurrentLocation(): PlatformLocation? = withContext(Dispatchers.Main.immediate) {
        var requestDelegate: CLLocationManagerDelegateProtocol? = null
        try {
            suspendCancellableCoroutine<PlatformLocation?> { continuation ->
                val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                    override fun locationManager(
                        manager: CLLocationManager,
                        didUpdateLocations: List<*>,
                    ) {
                        if (!continuation.isActive) return
                        val location = didUpdateLocations.lastOrNull() as? CLLocation
                        continuation.resume(location?.toPlatform())
                    }

                    override fun locationManager(
                        manager: CLLocationManager,
                        didFailWithError: NSError,
                    ) {
                        if (!continuation.isActive) return
                        continuation.resume(null)
                    }
                }

                requestDelegate = delegate
                locationDelegate = delegate
                locationManager.delegate = delegate
                if (continuation.isActive) {
                    locationManager.requestLocation()
                }
            }
        } finally {
            if (locationDelegate === requestDelegate) {
                locationManager.delegate = null
                locationManager.stopUpdatingLocation()
                locationDelegate = null
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun CLLocation.toPlatform(): PlatformLocation {
        return PlatformLocation(
            lat = coordinate.lat,
            lon = coordinate.lon,
            timestamp = timestamp.timeIntervalSince1970.toLong()
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
internal val CValue<CLLocationCoordinate2D>.lat: Double
    get() = useContents { this.latitude }

@OptIn(ExperimentalForeignApi::class)
internal val CValue<CLLocationCoordinate2D>.lon: Double
    get() = useContents { this.longitude }
