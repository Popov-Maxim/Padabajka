package com.padabajka.dating.core.permission.geo

import com.padabajka.dating.core.permission.GeoPermissionController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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
    private var permissionDelegate: CLLocationManagerDelegateProtocol? = null

    override suspend fun hasPermission(): Boolean {
        return hasPlatformPermission().also {
            _isPermissionGranted.value = it
        }
    }

    override suspend fun requestPermission(): Boolean = withContext(Dispatchers.Main.immediate) {
        var requestDelegate: CLLocationManagerDelegateProtocol? = null
        try {
            suspendCancellableCoroutine<Boolean> { cont ->
                val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                    override fun locationManager(
                        manager: CLLocationManager,
                        didChangeAuthorizationStatus: CLAuthorizationStatus
                    ) {
                        if (!cont.isActive) return

                        when (manager.authorizationStatus) {
                            kCLAuthorizationStatusAuthorizedAlways,
                            kCLAuthorizationStatusAuthorizedWhenInUse -> cont.resume(true)

                            kCLAuthorizationStatusDenied,
                            kCLAuthorizationStatusRestricted -> cont.resume(false)

                            else -> {}
                        }
                    }
                }

                requestDelegate = delegate
                permissionDelegate = delegate
                locationManager.delegate = delegate

                if (cont.isActive) {
                    when (locationManager.authorizationStatus) {
                        kCLAuthorizationStatusNotDetermined -> locationManager.requestWhenInUseAuthorization()
                        kCLAuthorizationStatusAuthorizedAlways,
                        kCLAuthorizationStatusAuthorizedWhenInUse -> cont.resume(true)

                        kCLAuthorizationStatusDenied,
                        kCLAuthorizationStatusRestricted -> cont.resume(false)
                    }
                }
            }.also {
                _isPermissionGranted.value = it
            }
        } finally {
            if (permissionDelegate === requestDelegate) {
                locationManager.delegate = null
                permissionDelegate = null
            }
        }
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
