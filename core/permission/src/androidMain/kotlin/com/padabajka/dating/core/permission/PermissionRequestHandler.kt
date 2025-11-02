package com.padabajka.dating.core.permission

import androidx.annotation.Size

fun interface PermissionRequestHandler {
    suspend fun request(@Size(min = 1) vararg permissions: String): Boolean
}
