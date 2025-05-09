package com.padabajka.dating

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.decompose.defaultComponentContext
import com.padabajka.dating.di.addPermissionRequester

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionLauncher =
            activityResultRegistry.register(
                "permission_request",
                ActivityResultContracts.RequestPermission()
            ) {}

        addPermissionRequester(permissionLauncher)
        setContent {
            App(defaultComponentContext())
        }
    }
}
