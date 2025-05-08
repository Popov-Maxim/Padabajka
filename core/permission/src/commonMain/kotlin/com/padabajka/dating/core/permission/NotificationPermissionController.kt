package com.padabajka.dating.core.permission

interface NotificationPermissionController {
    suspend fun hasPermission(): Boolean
    suspend fun requestPermission()
}
