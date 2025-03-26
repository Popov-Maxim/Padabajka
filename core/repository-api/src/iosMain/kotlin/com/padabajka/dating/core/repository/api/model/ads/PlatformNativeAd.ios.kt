package com.padabajka.dating.core.repository.api.model.ads

import platform.UIKit.UIStackView

actual interface PlatformNativeAd {
    fun bind(container: UIStackView)
}
