package com.padabajka.dating.core.permission.geo

import com.padabajka.dating.core.permission.GeoPermissionController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IosGeoPermissionController : GeoPermissionController {

    private val _isPermissionGranted: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(hasPlatformPermission())
    }

    private val locationManager = CLLocationManager()

    override suspend fun hasPermission(): Boolean {
        return hasPlatformPermission().also {
            _isPermissionGranted.value = it
        }
    }

    override suspend fun requestPermission(): Boolean = suspendCancellableCoroutine { cont ->

        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: CLAuthorizationStatus
            ) {
                when (manager.authorizationStatus) {
                    kCLAuthorizationStatusAuthorizedAlways,
                    kCLAuthorizationStatusAuthorizedWhenInUse -> cont.resume(true)

                    kCLAuthorizationStatusDenied,
                    kCLAuthorizationStatusRestricted -> cont.resume(false)

                    else -> {}
                }
            }
        }

        locationManager.delegate = delegate

        when (locationManager.authorizationStatus) {
            kCLAuthorizationStatusNotDetermined -> locationManager.requestWhenInUseAuthorization()
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> cont.resume(true)

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> cont.resume(false)
        }
    }.also {
        _isPermissionGranted.value = it
    }

    override fun isPermissionGranted(): Flow<Boolean> {
        return _isPermissionGranted.asSharedFlow()
    }

    private fun hasPlatformPermission(): Boolean {
        return when (locationManager.authorizationStatus) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> true

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted,
            kCLAuthorizationStatusNotDetermined -> false

            else -> false
        }
    }
}
