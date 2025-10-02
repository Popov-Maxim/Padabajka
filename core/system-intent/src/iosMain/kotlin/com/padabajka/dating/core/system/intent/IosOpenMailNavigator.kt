package com.padabajka.dating.core.system.intent

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class IosOpenMailNavigator : OpenMailNavigator {
    override suspend fun openMail() {
        val url = NSURL.URLWithString("mailto:") // TODO(login): check on real device
        val application = UIApplication.sharedApplication
        if (url != null && application.canOpenURL(url)) {
            application.openURL(url)
        }
    }
}
