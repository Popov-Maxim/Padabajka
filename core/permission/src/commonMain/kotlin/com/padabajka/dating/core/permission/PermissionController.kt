package com.padabajka.dating.core.permission

interface PermissionController {
    suspend fun hasPermission(): Boolean
    suspend fun requestPermission(): Boolean
}
