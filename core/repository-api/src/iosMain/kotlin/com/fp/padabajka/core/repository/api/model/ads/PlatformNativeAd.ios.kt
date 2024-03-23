package com.fp.padabajka.core.repository.api.model.ads

import platform.UIKit.UIView

actual interface PlatformNativeAd {
    fun bind(container: UIView)
}
