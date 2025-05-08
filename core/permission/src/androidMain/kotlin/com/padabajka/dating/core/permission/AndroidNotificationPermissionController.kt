package com.padabajka.dating.core.permission

import android.Manifest
import android.os.Build

class AndroidNotificationPermissionController(
    private val permissionHandler: PermissionHandler,
) : NotificationPermissionController {
    override suspend fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHandler.hasPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }
    }

    override suspend fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHandler.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
