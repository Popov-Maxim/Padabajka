package com.padabajka.dating.core.platform.data.geo

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

class IOSLocationProvider : PlatformLocationProvider {

    private val locationManager = CLLocationManager()

    init {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getCurrentLocation(): PlatformLocation? {
        val loc = locationManager.location
        return loc?.let {
            PlatformLocation(
                lat = it.coordinate.lat,
                lon = it.coordinate.lon,
                timestamp = NSDate().timeIntervalSince1970.toLong()
            )
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal val CValue<CLLocationCoordinate2D>.lat: Double
    get() = useContents { this.latitude }

@OptIn(ExperimentalForeignApi::class)
internal val CValue<CLLocationCoordinate2D>.lon: Double
    get() = useContents { this.longitude }
