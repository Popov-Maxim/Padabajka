package com.padabajka.dating

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.decompose.defaultComponentContext
import com.padabajka.dating.deeplink.SharedDeeplinkHandler
import com.padabajka.dating.di.addActivity
import com.padabajka.dating.di.addPermissionRequester

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this) // TODO: fix leak?
        val permissionLauncher =
            activityResultRegistry.register(
                "permission_request",
                ActivityResultContracts.RequestPermission()
            ) {}

        addPermissionRequester(permissionLauncher)
        setContent {
            App(defaultComponentContext())
        }

        intent?.run { handleDeeplink(this) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.run { handleDeeplink(this) }
    }

    private fun handleDeeplink(intent: Intent) {
        println("LOG: ${intent.data}")
        val uri = intent.data ?: return
        SharedDeeplinkHandler.handle(uri.toString())
    }
}
