package com.padabajka.dating.core.system.intent

import com.padabajka.dating.core.utils.printlnDev
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

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
}
