package com.padabajka.dating.core.platform.data.geo

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
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

    private val locationManager = CLLocationManager()

    init {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
    }

    override suspend fun cachedLocation(): PlatformLocation? {
        return locationManager.location?.toPlatform()
    }

    override suspend fun requestCurrentLocation(): PlatformLocation? {
        return suspendCancellableCoroutine { continuation ->

            locationManager.delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>,
                ) {
                    val location = didUpdateLocations.lastOrNull() as? CLLocation

                    continuation.resume(location?.toPlatform())
                }

                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError,
                ) {
                    continuation.resume(null)
                }
            }

            locationManager.requestLocation()
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
