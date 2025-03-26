package com.padabajka.dating.core.repository.api.model.ads

import android.view.ViewGroup

actual interface PlatformNativeAd {
    fun bind(container: ViewGroup)
}
