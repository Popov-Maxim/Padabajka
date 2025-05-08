package com.padabajka.dating.core.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate

class PermissionHandler(
    private val context: Context,
    launcherFactory: Factory<ActivityResultLauncher<String>>
) {

    private val launcher by launcherFactory.delegate()

    fun requestPermission(permission: String) {
        launcher.launch(permission)
    }

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
