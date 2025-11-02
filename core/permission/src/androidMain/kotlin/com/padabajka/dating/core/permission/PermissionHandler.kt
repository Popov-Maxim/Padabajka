package com.padabajka.dating.core.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate

class PermissionHandler(
    private val context: Context,
    permissionRequestHandlerFactory: Factory<PermissionRequestHandler>
) {

    private val launcher by permissionRequestHandlerFactory.delegate()

    suspend fun requestPermission(@Size(min = 1) vararg permissions: String): Boolean {
        return launcher.request(*permissions)
    }

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
