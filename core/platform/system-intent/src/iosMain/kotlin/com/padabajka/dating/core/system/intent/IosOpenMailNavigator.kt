package com.padabajka.dating.core.system.intent

import com.padabajka.dating.core.utils.printlnDev
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication
import kotlin.coroutines.resume

internal class IosOpenMailNavigator : OpenMailNavigator {
    override suspend fun openMail() {
        val url = NSURL.URLWithString("mailto:") ?: return
        val application = UIApplication.sharedApplication

        if (application.canOpenURL(url)) {
            application.openURL(
                url = url,
                options = emptyMap<Any?, Any?>(),
                completionHandler = {
                    printlnDev("OpenMailNavigator: $it")
                }
            )
        }
    }

    override suspend fun composeMail(address: String, subject: String, body: String): Boolean {
        val components = NSURLComponents().apply {
            scheme = "mailto"
            path = address
            queryItems = listOf(
                NSURLQueryItem(name = "subject", value = subject),
                NSURLQueryItem(name = "body", value = body)
            )
        }
        val url = components.URL ?: return false
        return suspendCancellableCoroutine { continuation ->
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any?>(),
                completionHandler = { opened ->
                    if (continuation.isActive) {
                        continuation.resume(opened)
                    }
                }
            )
        }
    }
}
