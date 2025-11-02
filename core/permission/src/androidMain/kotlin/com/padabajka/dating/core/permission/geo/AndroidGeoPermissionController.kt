package com.padabajka.dating.core.permission.geo

import android.Manifest
import com.padabajka.dating.core.permission.GeoPermissionController
import com.padabajka.dating.core.permission.PermissionHandler

class AndroidGeoPermissionController(
    private val permissionHandler: PermissionHandler
) : GeoPermissionController {
    override suspend fun hasPermission(): Boolean {
        return listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).any {
            permissionHandler.hasPermission(it)
        }
    }

    override suspend fun requestPermission(): Boolean {
        return permissionHandler.requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
