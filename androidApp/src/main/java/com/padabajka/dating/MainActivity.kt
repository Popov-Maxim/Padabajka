package com.padabajka.dating

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.decompose.defaultComponentContext
import com.padabajka.dating.core.permission.PermissionRequestHandler
import com.padabajka.dating.deeplink.SharedDeeplinkHandler
import com.padabajka.dating.di.addActivity
import com.padabajka.dating.di.addPermissionRequester
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this) // TODO: fix leak?
        val permissionRequestHandler = PermissionRequestHandler { permissions ->
            val result = requestPermissionsSuspend(*permissions)
            result.any { it.value }
        }
        addPermissionRequester(permissionRequestHandler)
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

    private suspend fun ComponentActivity.requestPermissionsSuspend(
        vararg permissions: String
    ): Map<String, Boolean> = suspendCancellableCoroutine { cont ->

        val key = "permission_request_${System.currentTimeMillis()}"
        val launcher = activityResultRegistry.register(
            key,
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            if (cont.isActive) cont.resume(result)
        }

        launcher.launch(permissions.toTypedArray())

        cont.invokeOnCancellation {
            launcher.unregister()
        }
    }

    private fun <T> Array<out T>.toTypedArray(): Array<T> {
        return this as Array<T>
    }
}
