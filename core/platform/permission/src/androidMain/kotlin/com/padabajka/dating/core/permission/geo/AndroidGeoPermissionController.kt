package com.padabajka.dating.core.permission.geo

import android.Manifest
import com.padabajka.dating.core.permission.GeoPermissionController
import com.padabajka.dating.core.permission.PermissionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidGeoPermissionController(
    private val permissionHandler: PermissionHandler
) : GeoPermissionController {

    private val _isPermissionGranted: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(hasPlatformPermission())
    }

    override suspend fun hasPermission(): Boolean {
        return hasPlatformPermission().also {
            _isPermissionGranted.value = it
        }
    }

    override suspend fun requestPermission(): Boolean {
        return permissionHandler.requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).also {
            _isPermissionGranted.value = it
        }
    }

    private fun hasPlatformPermission(): Boolean {
        return listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).any {
            permissionHandler.hasPermission(it)
        }
    }

    override fun isPermissionGranted(): Flow<Boolean> {
        return _isPermissionGranted.asStateFlow()
    }
}
