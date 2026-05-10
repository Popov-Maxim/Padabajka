package com.padabajka.dating.core.permission

import kotlinx.coroutines.flow.Flow

interface GeoPermissionController : PermissionController {
    fun isPermissionGranted(): Flow<Boolean>
}
